package com.EntertainmentViet.backend.domain.values;

import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@NoArgsConstructor
@Getter
@Setter
public class LocationAddress {
  @NotNull
  private String street;

  @NotNull
  private String district;

  @NotNull
  private String city;
}
