package com.EntertainmentViet.backend.features.admin.dto.talent;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@SuperBuilder
@NoArgsConstructor
@Getter
@Setter
public class ScoreOperandDto {
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private String id;
  private String name;
  private Double rate;
  private Integer multiply;
  private Boolean active;
  private List<String> proof;
}
