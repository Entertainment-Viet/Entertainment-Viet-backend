package com.EntertainmentViet.backend.features.organizer.dto;

import com.EntertainmentViet.backend.features.booking.dto.BookingDto;
import com.EntertainmentViet.backend.features.common.dto.IdentifiableDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@SuperBuilder
@Getter
@Setter
public class EventOpenPositionDto extends IdentifiableDto {

  private UUID eventId;

  private JobOfferDto jobOffer;

  private List<BookingDto> applicants;
}
