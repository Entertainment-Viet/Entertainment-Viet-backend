package com.EntertainmentViet.backend.features.talent.dto.packagetalent;

import com.EntertainmentViet.backend.domain.standardTypes.Currency;
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

  private Double minPrice;

  private Double maxPrice;

  private Currency currency;
}
