package com.EntertainmentViet.backend.features.admin.dto.talent;

import java.util.List;
import java.util.UUID;

import com.EntertainmentViet.backend.features.scoresystem.dto.PriorityScoreDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@NoArgsConstructor
@SuperBuilder
@Getter
@Setter
public class UpdateAdminTalentDto {

  private String displayName;

  private String phoneNumber;

  private String email;

  private UUID address;

  private String bio;

  private List<UUID> offerCategories;

  private String extensions;

  private List<PriorityScoreDto> priorityScores;
}
