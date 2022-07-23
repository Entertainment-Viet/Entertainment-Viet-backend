package com.EntertainmentViet.backend.features.security.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class LoginTokenDto {
  private String accessToken;
  private Integer expiresIn;
  private String refreshToken;
  private Integer refreshExpiresIn;
}
