package com.EntertainmentViet.backend.features.organizer.boundary.event;

import com.EntertainmentViet.backend.domain.businessLogic.FinanceUtils;
import com.EntertainmentViet.backend.domain.entities.booking.Booking;
import com.EntertainmentViet.backend.domain.entities.organizer.Event;
import com.EntertainmentViet.backend.domain.entities.organizer.EventOpenPosition;
import com.EntertainmentViet.backend.domain.entities.organizer.Organizer;
import com.EntertainmentViet.backend.domain.standardTypes.BookingStatus;
import com.EntertainmentViet.backend.domain.standardTypes.PaymentType;
import com.EntertainmentViet.backend.domain.standardTypes.WorkType;
import com.EntertainmentViet.backend.features.booking.dto.booking.BookingMapper;
import com.EntertainmentViet.backend.features.booking.dto.booking.ListBookingResponseDto;
import com.EntertainmentViet.backend.features.booking.dto.booking.ListOrganizerBookingParamDto;
import com.EntertainmentViet.backend.features.common.dto.CustomPage;
import com.EntertainmentViet.backend.features.common.utils.EntityValidationUtils;
import com.EntertainmentViet.backend.features.common.utils.RestUtils;
import com.EntertainmentViet.backend.features.organizer.dao.event.EventOpenPositionRepository;
import com.EntertainmentViet.backend.features.organizer.dao.event.EventRepository;
import com.EntertainmentViet.backend.features.organizer.dao.organizer.OrganizerRepository;
import com.EntertainmentViet.backend.features.organizer.dto.event.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventService implements EventBoundary {

  private final EventRepository eventRepository;

  private final EventOpenPositionRepository eventOpenPositionRepository;

  private final OrganizerRepository organizerRepository;

  private final EventMapper eventMapper;

  private final BookingMapper bookingMapper;

  @Override
  public CustomPage<ReadEventDto> findAll(ListEventParamDto paramDto, Pageable pageable) {
    var dataPage = RestUtils.toLazyLoadPageResponse(
        eventRepository.findAll(paramDto, pageable)
            .map(eventMapper::toReadDto)
    );

    if (eventRepository.findAll(paramDto, pageable.next()).hasContent()) {
      dataPage.getPaging().setLast(false);
    }

    return dataPage;
  }

  @Override
  public Page<ReadEventDto> findByOrganizerUid(UUID uid, ListEventParamDto paramDto, Pageable pageable) {
    var dtoList = eventRepository.findByOrganizerUid(uid, paramDto, pageable).stream()
        .map(eventMapper::toReadDto)
        .collect(Collectors.toList());

    return RestUtils.getPageEntity(dtoList, pageable);
  }

  @Override
  public Optional<ReadEventDto> findByUid(UUID uid) {
    Event event = eventRepository.findByUid(uid).orElse(null);
    if (!EntityValidationUtils.isEventWithUidExist(event, uid)) {
      return Optional.empty();
    }
    return Optional.ofNullable(eventMapper.toReadDto(event));

  }

  @Override
  public Optional<ReadEventDto> findByOrganizerUidAndUid(UUID organizerUid, UUID uid) {
    Event event = eventRepository.findByUid(uid).orElse(null);
    if (!EntityValidationUtils.isEventWithUidExist(event, uid)) {
      return Optional.empty();
    }

    if (!EntityValidationUtils.isEventBelongToOrganizerWithUid(event, organizerUid)) {
      return Optional.empty();
    }

    return Optional.ofNullable(eventMapper.toReadDto(event));
  }

  @Override
  @Transactional
  public Optional<UUID> create(CreateEventDto createEventDto, UUID organizerUid) {
    Organizer organizer = organizerRepository.findByUid(organizerUid).orElse(null);
    Event createEvent = eventMapper.fromCreateDtoToModel(createEventDto);
    createEvent.setOrganizer(organizer);
    createEvent.getEventDetail().setEvent(createEvent);

    return Optional.ofNullable(eventRepository.save(createEvent).getUid());
  }

  @Override
  public Optional<UUID> update(UpdateEventDto updateEventDto, UUID organizerUid, UUID uid) {
    Event event = eventRepository.findByUid(uid).orElse(null);
    if (!EntityValidationUtils.isEventWithUidExist(event, uid)) {
      return Optional.empty();
    }

    if (!EntityValidationUtils.isEventBelongToOrganizerWithUid(event, organizerUid)) {
      return Optional.empty();
    }

    Event newEvent = eventMapper.fromUpdateDtoToModel(updateEventDto);
    event.updateInfo(newEvent);
    return Optional.ofNullable(eventRepository.save(event).getUid());
  }

  @Override
  public boolean delete(UUID uid, UUID organizerUid) {
    Event event = eventRepository.findByUid(uid).orElse(null);
    if (!EntityValidationUtils.isEventWithUidExist(event, uid)) {
      return false;
    }

    if (!EntityValidationUtils.isEventBelongToOrganizerWithUid(event, organizerUid)) {
      return false;
    }

    event.setArchived(Boolean.TRUE);
    eventRepository.save(event);
    return true;
  }

  @Override
  public ListBookingResponseDto listBooking(boolean isOwnerUser, UUID organizerId, ListOrganizerBookingParamDto paramDto, Pageable pageable) {
    var eventList = eventRepository.findByOrganizerUid(organizerId, null, pageable);
    Set<Booking> bookingList = new HashSet<>();
    for (Event event : eventList) {
      for (EventOpenPosition position : event.getOpenPositions()) {
        EventOpenPosition fetchedOpenPosition = eventOpenPositionRepository.findByUid(position.getUid()).orElse(null);
        bookingList.addAll(fetchedOpenPosition.getApplicants().stream().filter(booking -> isBookingCompliant(booking, paramDto)).toList());
      }
    }

    var dtoList = bookingList.stream()
        .map(bookingMapper::toReadDto)
        .map(dto -> bookingMapper.checkPermission(dto, isOwnerUser))
        .toList();
    var dataPage = RestUtils.getPageEntity(dtoList, pageable);

    var dataOffset = RestUtils.getPagingOffer(bookingList.stream().toList(), pageable);
    var financeReport = FinanceUtils.generateOrganizerBookingReport(bookingList.stream().toList().subList(dataOffset.getStart(), dataOffset.getEnd()));
    return ListBookingResponseDto.builder()
        .unpaidSum(financeReport.getUnpaid())
        .price(financeReport.getPrice())
        .tax(financeReport.getTax())
        .total(financeReport.getTotal())
        .bookings(RestUtils.toPageResponse(dataPage))
        .build();
  }

  private boolean isBookingCompliant(Booking booking, ListOrganizerBookingParamDto paramDto) {
    // List all booking have performanceStartTime equal or after the paramDto.startTime
    if (paramDto.getStartTime() != null && paramDto.getEndTime() == null) {
      if (booking.getJobDetail().getPerformanceEndTime().isBefore(paramDto.getStartTime())) {
        return false;
      }
      // List all booking have performanceEndTime equal or before the paramDto.startTime
    } else if (paramDto.getStartTime() == null && paramDto.getEndTime() != null) {
      if (booking.getJobDetail().getPerformanceStartTime().isAfter(paramDto.getEndTime())) {
        return false;
      }
      // List all booking have performance duration within paramDto.startTime and paramDto.endTime
    } else if (paramDto.getStartTime() != null && paramDto.getEndTime() != null) {
      if (booking.getJobDetail().getPerformanceStartTime().isAfter(paramDto.getEndTime()) ||
          booking.getJobDetail().getPerformanceEndTime().isBefore(paramDto.getStartTime())) {
        return false;
      }
    }
    if (paramDto.getPaid() != null) {
      if (booking.isPaid() != paramDto.getPaid()) {
        return false;
      }
    }
    if (paramDto.getStatus() != null) {
      if (!booking.getStatus().equals(BookingStatus.ofI18nKey(paramDto.getStatus()))) {
        return false;
      }
    }
    if (paramDto.getPaymentType() != null) {
      if (!booking.getPaymentType().equals(PaymentType.ofI18nKey(paramDto.getPaymentType()))) {
        return false;
      }
    }
    if (paramDto.getTalent() != null) {
      if (!booking.getTalent().getDisplayName().contains(paramDto.getTalent())){
        return false;
      }
    }
    if (paramDto.getCategory() != null) {
      if (!booking.getJobDetail().getCategory().getUid().equals(paramDto.getCategory())) {
        return false;
      }
    }
    if (paramDto.getWorkType() != null) {
      if (!booking.getJobDetail().getWorkType().equals(WorkType.ofI18nKey(paramDto.getWorkType()))) {
        return false;
      }
    }

    return true;
  }
}
