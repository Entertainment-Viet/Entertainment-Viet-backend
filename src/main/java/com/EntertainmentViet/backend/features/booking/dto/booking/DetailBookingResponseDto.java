package com.EntertainmentViet.backend.features.booking.dto.booking;

import com.EntertainmentViet.backend.domain.values.FinanceReport;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@NoArgsConstructor
@SuperBuilder
@Getter
@Setter
public class DetailBookingResponseDto {

  @JsonUnwrapped
  private ReadBookingDto readBookingDto;

  @JsonUnwrapped
  private FinanceReport financeReport;
}
