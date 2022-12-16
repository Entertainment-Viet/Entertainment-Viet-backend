package com.EntertainmentViet.backend.domain.values;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class FinanceReport {
  Double unpaid;
  Double price;
  Double tax;
  Double total;
}
