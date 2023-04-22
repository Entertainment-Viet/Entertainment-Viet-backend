package com.EntertainmentViet.backend.features.security.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
public class CredentialDto {
  @Builder.Default
  private String type = "password";

  @Builder.Default
  private Boolean temporary = false;

  private String value;
}
