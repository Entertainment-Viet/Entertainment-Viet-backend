package com.EntertainmentViet.backend.features.organizer.dto;

import com.EntertainmentViet.backend.features.admin.dto.OrganizerFeedBackDto;
import com.EntertainmentViet.backend.features.booking.dto.BookingDto;
import com.EntertainmentViet.backend.features.common.dto.UserDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.UUID;

@SuperBuilder
@NoArgsConstructor
@Getter
@Setter
public class OrganizerDto extends UserDto {

  private List<JobOfferDto> jobOffers;

  private List<EventDto> events;

  private List<BookingDto> bookings;

  private List<OrganizerFeedBackDto> feedbacks;

  private List<UUID> shoppingCart;
}
