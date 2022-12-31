package com.EntertainmentViet.backend.domain.entities.config;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class FinanceConfig {
  private Double talentFee;
  private Double organizerFee;
  private Double vat;
  private Double pit;
}
