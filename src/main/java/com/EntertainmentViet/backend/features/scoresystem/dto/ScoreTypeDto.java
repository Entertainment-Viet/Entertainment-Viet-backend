package com.EntertainmentViet.backend.features.scoresystem.dto;

import com.EntertainmentViet.backend.features.common.dto.IdentifiableDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@NoArgsConstructor
@Getter
@Setter
public class ScoreTypeDto extends IdentifiableDto {
  private String name;
  private Double rate;
}
