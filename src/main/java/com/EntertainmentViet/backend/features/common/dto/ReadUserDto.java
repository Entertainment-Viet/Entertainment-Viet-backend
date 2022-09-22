package com.EntertainmentViet.backend.features.common.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;


import java.time.OffsetDateTime;

@NoArgsConstructor
@SuperBuilder
@Getter
@Setter
public abstract class ReadUserDto extends IdentifiableDto {

  private String displayName;

  private String phoneNumber;

  private String email;

  private String address;

  private String bio;

  private OffsetDateTime createdAt;

  private String userState;

  private String extensions;
}
