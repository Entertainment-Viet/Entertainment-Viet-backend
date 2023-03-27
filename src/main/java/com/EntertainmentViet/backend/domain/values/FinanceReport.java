package com.EntertainmentViet.backend.domain.values;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FinanceReport {

  @Builder.Default
  Double price = 0.0;

  @Builder.Default
  Double fee = 0.0;

  @Builder.Default
  Double tax = 0.0;

  @Builder.Default
  Double total = 0.0;

  public FinanceReport combineWith(FinanceReport otherReport) {
    setPrice(price + otherReport.getPrice());
    setFee(fee + otherReport.getFee());
    setTax(tax + otherReport.getTax());
    setTotal(total + otherReport.getTotal());
    return this;
  }
}
