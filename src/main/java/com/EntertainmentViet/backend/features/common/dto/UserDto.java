package com.EntertainmentViet.backend.features.common.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.Instant;

@NoArgsConstructor
@SuperBuilder
@Getter
@Setter
public abstract class UserDto extends AccountDto {

  private String phoneNumber;

  private String email;

  private String address;

  private String bio;

  private Instant createdAt;

  private String userState;

  private String extensions;
}
