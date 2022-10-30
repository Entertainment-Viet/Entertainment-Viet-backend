package com.EntertainmentViet.backend.domain.entities.organizer;

import com.EntertainmentViet.backend.domain.entities.User;
import com.EntertainmentViet.backend.domain.entities.admin.OrganizerFeedback;
import com.EntertainmentViet.backend.domain.entities.admin.OrganizerFeedback_;
import com.EntertainmentViet.backend.domain.entities.booking.Booking;
import com.EntertainmentViet.backend.domain.entities.booking.JobDetail;
import com.EntertainmentViet.backend.domain.entities.talent.Package;
import com.EntertainmentViet.backend.domain.entities.talent.Talent;
import com.EntertainmentViet.backend.domain.entities.talent.TalentDetail_;
import com.EntertainmentViet.backend.domain.standardTypes.BookingStatus;
import com.EntertainmentViet.backend.domain.standardTypes.PaymentType;
import com.EntertainmentViet.backend.domain.standardTypes.UserState;
import com.EntertainmentViet.backend.domain.values.Price;
import com.EntertainmentViet.backend.exception.EntityNotFoundException;
import com.EntertainmentViet.backend.features.common.utils.SecurityUtils;
import com.EntertainmentViet.backend.features.security.roles.PaymentRole;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@SuperBuilder
@NoArgsConstructor
@Getter
@Setter
@Entity
@Slf4j
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
public class Organizer extends User {

  @OneToMany(mappedBy = JobOffer_.ORGANIZER, cascade = CascadeType.ALL, orphanRemoval = true)
  private List<JobOffer> jobOffers;

  @OneToMany(mappedBy = Event_.ORGANIZER, cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Event> events;

  @OneToMany(mappedBy = Event_.ORGANIZER, cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Booking> bookings;

  @OneToMany(mappedBy = OrganizerFeedback_.ORGANIZER, cascade = CascadeType.ALL, orphanRemoval = true)
  private List<OrganizerFeedback> feedbacks;

  @OneToMany(mappedBy = OrganizerShoppingCart_.ORGANIZER, cascade = CascadeType.ALL, orphanRemoval = true)
  private List<OrganizerShoppingCart> shoppingCart;

  @OneToOne(mappedBy = OrganizerDetail_.ORGANIZER, cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = false, orphanRemoval = true)
  @PrimaryKeyJoinColumn
  private OrganizerDetail organizerDetail;

  public void addJobOffer(JobOffer jobOffer) {
    jobOffers.add(jobOffer);
    jobOffer.setOrganizer(this);
  }

  public void removeJobOffer(JobOffer jobOffer) {
    jobOffers.remove(jobOffer);
    jobOffer.setOrganizer(null);
  }

  public void addEvent(Event event) {
    events.add(event);
    event.setOrganizer(this);
  }

  public void disableEvent(UUID eventUid) {
    events.stream()
        .filter(event -> event.getUid().equals(eventUid))
        .findFirst()
        .ifPresent( event -> event.setIsActive(false));
  }

  public void removeEvent(Event event) {
    events.remove(event);
    event.setOrganizer(null);
  }

  public void addBooking(Booking booking) {
    bookings.add(booking);
    booking.setOrganizer(this);
  }

  public void removeBooking(Booking booking) {
    bookings.remove(booking);
    booking.setOrganizer(null);
  }

  public void updateBookingInfo(UUID bookingUid, Booking newBookingInfo) {
    bookings.stream()
        .filter(booking -> booking.getUid().equals(bookingUid))
        .findAny()
        .ifPresentOrElse(
            booking -> {
              // organizer only allow to update price.max
              newBookingInfo.getJobDetail().getPrice().setMin(null);
              booking.updateInfo(newBookingInfo);
              booking.setStatus(BookingStatus.TALENT_PENDING);
            },
            () -> {throw new EntityNotFoundException("Booking", bookingUid);}
        );
  }

  public void acceptBooking(UUID bookingUid) {
    bookings.stream()
        .filter(booking -> booking.getUid().equals(bookingUid))
        .filter(booking -> booking.getStatus().equals(BookingStatus.ORGANIZER_PENDING))
        .findAny()
        .ifPresentOrElse(
            booking -> {
              var currentPrice = booking.getJobDetail().getPrice();
              currentPrice.setMax(currentPrice.getMin());
              booking.setStatus(BookingStatus.CONFIRMED);
              booking.setConfirmedAt(OffsetDateTime.now());
            },
            () -> {throw new EntityNotFoundException("Booking", bookingUid);}
        );
  }

  public void rejectBooking(UUID bookingUid) {
    bookings.stream()
        .filter(booking -> booking.getUid().equals(bookingUid))
        .filter(booking -> booking.getStatus().equals(BookingStatus.ORGANIZER_PENDING))
        .findAny()
        .ifPresentOrElse(
            booking -> booking.setStatus(BookingStatus.CANCELLED),
            () -> {throw new EntityNotFoundException("Booking", bookingUid);}
        );
  }

  public void finishBooking(UUID bookingUid) {
    bookings.stream()
        .filter(booking -> booking.getUid().equals(bookingUid))
        .filter(booking -> booking.getStatus().equals(BookingStatus.CONFIRMED) || booking.getStatus().equals(BookingStatus.TALENT_FINISH))
        .findAny()
        .ifPresentOrElse(
            booking -> {
              if (booking.getStatus().equals(BookingStatus.CONFIRMED)) {
                booking.setStatus(BookingStatus.ORGANIZER_FINISH);
              } else if (booking.getStatus().equals(BookingStatus.TALENT_FINISH)) {
                booking.setStatus(BookingStatus.FINISHED);
              }
            },
            () -> {throw new EntityNotFoundException("Booking", bookingUid);}
        );
  }

  public void addFeedback(OrganizerFeedback feedback) {
    feedbacks.add(feedback);
    feedback.setOrganizer(this);
  }

  public void removeFeedback(OrganizerFeedback feedback) {
    feedbacks.remove(feedback);
    feedback.setOrganizer(null);
  }

  public boolean addPackageToCart(Package talentPackage, Double price) {
    OrganizerShoppingCart cartItem = new OrganizerShoppingCart(this, talentPackage, price);
    var overlapCartItem = shoppingCart.stream().filter(item -> item.getId().equals(cartItem.getId())).toList();
    if (!overlapCartItem.isEmpty()) {
      return false;
    }
    shoppingCart.add(cartItem);
    return true;
  }

  public boolean checkValidShoppingCart() {
    var invalidPackage = shoppingCart.stream()
        .filter(Predicate.not(OrganizerShoppingCart::checkValidCartItem))
        .collect(Collectors.toList());
    if (invalidPackage.size() != 0) {
      log.warn("Exist invalid cart item in shopping cart");
      return false;
    }
    return true;
  }

  public void clearCart() {
    shoppingCart.clear();
  }

  public void pay(Booking booking) {
    if (SecurityUtils.hasRole(PaymentRole.PAY_ORGANIZER_CASH.name()) && booking.checkIfConfirmed()) {
      booking.setPaid(true);
    }
  }

  public void makeBookingFromJobOffer(JobOffer jobOffer, Talent talent) {
    Booking newBooking = jobOffer.sendOffer(talent);
    addBooking(newBooking);
    talent.addBooking(newBooking);
  }

  public Double computeUnpaidSum() {
    return bookings.stream()
        .filter(booking -> booking.getStatus().equals(BookingStatus.FINISHED))
        .filter(booking -> !booking.isPaid())
        .map(Booking::getJobDetail)
        .map(JobDetail::getPrice)
        .map(Price::getMax)
        .mapToDouble(Double::doubleValue)
        .sum();
  }

  public Organizer updateInfo(Organizer newData) {
    if (newData.getOrganizerDetail() != null) {
      getOrganizerDetail().updateBasicInfo(newData.getOrganizerDetail());
    }
    if (newData.getDisplayName() != null) {
      setDisplayName(newData.getDisplayName());
    }
    if (newData.getJobOffers() != null) {
      setJobOffers(newData.getJobOffers());
    }
    if (newData.getEvents() != null) {
      setEvents(newData.getEvents());
    }
    if (newData.getFeedbacks() != null) {
      setFeedbacks(newData.getFeedbacks());
    }
    if (newData.getShoppingCart() != null) {
      setShoppingCart(newData.getShoppingCart());
    }
    return this;
  }

  public Organizer requestKycInfoChange(Organizer newData) {
    getOrganizerDetail().updateKycInfo(newData.getOrganizerDetail());
    setUserState(UserState.PENDING);
    return this;
  }

  @Override
  protected boolean checkIfUserVerifiable() {
    if (!getUserState().equals(UserState.PENDING) || !getUserState().equals(UserState.UNVERIFIED)) {
      return false;
    }

    if (getAccountType() == null) {
      return false;
    }

    if (!organizerDetail.isAllKycFilled()) {
      return false;
    }

    return true;
  }
}
