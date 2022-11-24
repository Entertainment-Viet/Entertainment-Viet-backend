package com.EntertainmentViet.backend.features.common.dto;

import java.time.OffsetDateTime;

import com.EntertainmentViet.backend.features.booking.dto.location.LocationDto;
import com.EntertainmentViet.backend.features.booking.dto.location.ReadLocationDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@NoArgsConstructor
@SuperBuilder
@Getter
@Setter
public abstract class ReadUserDto extends IdentifiableDto {

  private String accountType;

  private String displayName;

  private String bio;

  private OffsetDateTime createdAt;

  private String userState;

  private String phoneNumber;

  private String email;

  private ReadLocationDto address;

  private String taxId;

  private String bankAccountNumber;

  private String bankAccountOwner;

  private String bankName;

  private String bankBranchName;

  private String extensions;
}
