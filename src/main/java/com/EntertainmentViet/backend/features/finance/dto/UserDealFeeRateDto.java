package com.EntertainmentViet.backend.features.finance.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@NoArgsConstructor
@Getter
@Setter
public class UserDealFeeRateDto {
  private Double feeRate;
}
