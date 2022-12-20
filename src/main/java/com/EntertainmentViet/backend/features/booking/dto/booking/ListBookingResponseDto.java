package com.EntertainmentViet.backend.features.booking.dto.booking;

import com.EntertainmentViet.backend.features.common.dto.CustomPage;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@NoArgsConstructor
@SuperBuilder
@Getter
@Setter
public class ListBookingResponseDto {
  private Double unpaidSum;
  private Double price;
  private Double tax;
  private Double total;
  private CustomPage<ReadBookingDto> bookings;
}
