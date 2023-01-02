package com.EntertainmentViet.backend.features.scoresystem.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.UUID;

@SuperBuilder
@NoArgsConstructor
@Getter
@Setter
public class ReadScoreRewardListDto {
  private UUID scoreTypeId;
  private String scoreTypeName;
  private String achievement;
  private Boolean approved;
  private List<String> proof;
}
