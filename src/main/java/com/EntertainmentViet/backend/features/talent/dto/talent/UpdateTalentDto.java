package com.EntertainmentViet.backend.features.talent.dto.talent;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotNull;

@NoArgsConstructor
@SuperBuilder
@Getter
@Setter
public class UpdateTalentDto {

  private String displayName;

  private String phoneNumber;

  private String email;

  private String address;

  private String bio;

  private String extensions;
}
