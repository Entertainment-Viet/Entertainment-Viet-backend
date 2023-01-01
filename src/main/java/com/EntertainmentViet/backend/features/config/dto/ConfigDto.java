package com.EntertainmentViet.backend.features.config.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@NoArgsConstructor
@SuperBuilder
@Getter
@Setter
public class ConfigDto {
  private String propertyName;
  private String propertyValue;
}
