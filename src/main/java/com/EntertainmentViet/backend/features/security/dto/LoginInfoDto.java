package com.EntertainmentViet.backend.features.security.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
public class LoginInfoDto {
  private String username;
  private String password;
}
