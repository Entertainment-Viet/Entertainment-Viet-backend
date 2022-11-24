package com.EntertainmentViet.backend.features.common.dto;

import java.util.UUID;

import com.EntertainmentViet.backend.features.booking.dto.location.InputLocationDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@NoArgsConstructor
@SuperBuilder
@Getter
@Setter
public class UpdateUserKycInfoDto {

  private String accountType;

  private String phoneNumber;

  private InputLocationDto address;

  private String taxId;

  private String bankAccountNumber;

  private String bankAccountOwner;

  private String bankName;

  private String bankBranchName;
}
