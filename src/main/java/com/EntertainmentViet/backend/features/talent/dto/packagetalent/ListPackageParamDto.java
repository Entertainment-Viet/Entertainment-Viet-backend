package com.EntertainmentViet.backend.features.talent.dto.packagetalent;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ListPackageParamDto {

  private String name;

  private Boolean isActive;

  // Talent name
  private String talent;

  // Number of order
  private Integer orderCount;
}
