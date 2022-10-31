package com.EntertainmentViet.backend.features.common.dto;

import com.EntertainmentViet.backend.domain.values.LocationAddress;
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

  private LocationAddress address;

  private String taxId;

  private String bankAccountNumber;

  private String bankAccountOwner;

  private String bankName;

  private String bankBranchName;
}
