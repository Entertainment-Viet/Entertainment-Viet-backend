package com.EntertainmentViet.backend.features.booking.dto.booking;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.OffsetDateTime;
import java.util.UUID;

@AllArgsConstructor
@Getter
public class ListEventBookingParamDto {

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime startTime;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime endTime;

  private Boolean paid;

  private String status;

  private String paymentType;

  private UUID category;

  private String workType;

  private String bookingCode;

  // talent name
  private String talent;

  /**
   * It is false by default, it lists out bookings that from non-archived talent
   */
  private Boolean withArchived = Boolean.FALSE;
}
