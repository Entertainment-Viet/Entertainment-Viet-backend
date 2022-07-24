package com.EntertainmentViet.backend.features.security.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@NoArgsConstructor
@Getter
@Setter
public class LoginResponseDto {
  private String accessToken;
  private Instant accessExpiresIn;
  private String refreshToken;
  private Instant refreshExpiresIn;
  private String idToken;
}
