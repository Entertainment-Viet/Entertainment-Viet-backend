package com.EntertainmentViet.backend.features.scoresystem.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@SuperBuilder
@NoArgsConstructor
@Getter
@Setter
public class UpdateScoreRewardListDto {
  private Long scoreTypeId;
  private String achievement;
  private List<String> proof;
}
