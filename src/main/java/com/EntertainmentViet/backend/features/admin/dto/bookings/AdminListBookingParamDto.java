package com.EntertainmentViet.backend.features.admin.dto.bookings;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.OffsetDateTime;
import java.util.UUID;

@AllArgsConstructor
@Getter
public class AdminListBookingParamDto {

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime startTime;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime endTime;

  private Boolean paid;

  private String status;

  private String paymentType;

  private UUID category;

  private String workType;

  // organizer name
  private String organizer;

  // talent name
  private String talent;
}