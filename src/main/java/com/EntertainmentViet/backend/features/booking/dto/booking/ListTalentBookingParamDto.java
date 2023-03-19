package com.EntertainmentViet.backend.features.booking.dto.booking;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@Getter
public class ListTalentBookingParamDto {

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime startTime;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime endTime;

  private Boolean paid;

  private List<String> status;

  private String paymentType;

  private UUID category;

  private String workType;

  private String bookingCode;

  // organizer name
  private String organizer;

  /**
   * It is false by default, it lists out bookings that from non-archived talent
   */
  private Boolean withArchived = Boolean.FALSE;
}
