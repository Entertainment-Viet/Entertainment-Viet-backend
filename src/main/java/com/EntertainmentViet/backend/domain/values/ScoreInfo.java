package com.EntertainmentViet.backend.domain.values;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@SuperBuilder
@NoArgsConstructor
@Getter
@Setter
public class ScoreInfo {
  private String name;
  private Double rate;
  private Integer multiply;
  private Boolean active;
  private List<String> proof;
}
