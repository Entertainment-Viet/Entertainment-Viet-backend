package com.EntertainmentViet.backend.features.admin.dto.talent;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@SuperBuilder
@Getter
@Setter
public class UpdateAdminTalentDto {

  private String displayName;

  private String phoneNumber;

  private String email;

  private String address;

  private String bio;

  private List<UUID> offerCategories;

  private String extensions;

  private List<ScoreOperandDto> scoreSystem;
}