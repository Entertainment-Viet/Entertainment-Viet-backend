package com.EntertainmentViet.backend.features.admin.dto.bookings;

import com.EntertainmentViet.backend.features.booking.dto.booking.ReadBookingDto;
import com.EntertainmentViet.backend.features.common.dto.CustomPage;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@NoArgsConstructor
@SuperBuilder
@Getter
@Setter
public class AdminListBookingResponseDto {
  private Double unpaidSum;
  private Double price;
  private Double fee;
  private Double tax;
  private Double total;
  private CustomPage<ReadBookingDto> bookings;
}
