package com.EntertainmentViet.backend.domain.values;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ScoreOperand {
  private String name;
  private Double rate;
  private Integer quantity;
  private Boolean active;
}
