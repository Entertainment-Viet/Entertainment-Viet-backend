package com.EntertainmentViet.backend.features.admin.dto.talent;

import com.EntertainmentViet.backend.features.scoresystem.dto.PriorityScoreDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@NoArgsConstructor
@SuperBuilder
@Getter
@Setter
public class UpdateAdminTalentDto {

  private List<PriorityScoreDto> priorityScores;

  private Boolean editorChoice;

  private List<String> hashTag;
}
