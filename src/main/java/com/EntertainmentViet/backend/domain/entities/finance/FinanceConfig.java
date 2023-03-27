package com.EntertainmentViet.backend.domain.entities.finance;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder(toBuilder = true)
public class FinanceConfig implements Cloneable {
  private Double talentFee;
  private Double organizerFee;
  private Double vat;
  private Double pit;

  public boolean ifValid() {
    return talentFee != null && organizerFee != null && vat != null && pit != null;
  }

  @Override
  public FinanceConfig clone() {
    return this.toBuilder().build();
  }
}
