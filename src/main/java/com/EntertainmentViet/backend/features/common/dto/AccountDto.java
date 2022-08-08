package com.EntertainmentViet.backend.features.common.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@NoArgsConstructor
@SuperBuilder
@Getter
@Setter
public abstract class AccountDto extends IdentifiableDto {

  private String displayName;

}
