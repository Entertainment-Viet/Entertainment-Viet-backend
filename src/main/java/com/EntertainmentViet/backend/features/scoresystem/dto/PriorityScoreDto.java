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
public class PriorityScoreDto {
  private Long id;
  private String achievement;
  private UUID scoreTypeId;
  private List<String> proof;
  private Boolean approved;
}
