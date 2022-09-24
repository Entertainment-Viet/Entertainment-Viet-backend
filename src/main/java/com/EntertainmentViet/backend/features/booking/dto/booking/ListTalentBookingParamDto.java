package com.EntertainmentViet.backend.features.booking.dto.booking;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.OffsetDateTime;

@AllArgsConstructor
@Getter
public class ListTalentBookingParamDto {

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime startTime;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime endTime;

  private Boolean paid;

  private String status;

  // organizer name
  private String organizer;
}
