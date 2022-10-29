package com.EntertainmentViet.backend.features.admin.dto.talent;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@SuperBuilder
@NoArgsConstructor
@Getter
@Setter
public class ScoreToggleDto {
  private String id;
  private String name;
  private Boolean active;
  private List<String> proof;
}
