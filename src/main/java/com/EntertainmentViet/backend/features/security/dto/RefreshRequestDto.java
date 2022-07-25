package com.EntertainmentViet.backend.features.security.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class RefreshRequestDto {
  private String idToken;
  private String refreshToken;
}
