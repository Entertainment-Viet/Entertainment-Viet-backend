package com.EntertainmentViet.backend.features.booking.dto.jobdetail;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotNull;

@NoArgsConstructor
@SuperBuilder
@Getter
@Setter
public class PriceDto {

  @NotNull
  private Double min;

  @NotNull
  private Double max;

  @NotNull
  private String currency;
}
