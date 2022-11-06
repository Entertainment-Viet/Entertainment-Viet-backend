package com.EntertainmentViet.backend.features.scoresystem.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@NoArgsConstructor
@Getter
@Setter
public class ScoreTypeDto {
  private Long id;
  private String name;
  private Double rate;
}
