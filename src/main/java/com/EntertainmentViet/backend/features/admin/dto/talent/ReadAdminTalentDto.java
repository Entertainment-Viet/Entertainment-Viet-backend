package com.EntertainmentViet.backend.features.admin.dto.talent;

import com.EntertainmentViet.backend.features.booking.dto.category.ReadCategoryDto;
import com.EntertainmentViet.backend.features.common.dto.ReadUserDto;
import com.EntertainmentViet.backend.features.scoresystem.dto.PriorityScoreDto;
import com.EntertainmentViet.backend.features.talent.dto.packagetalent.ReadPackageDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.Set;

@SuperBuilder
@NoArgsConstructor
@Getter
@Setter
public class ReadAdminTalentDto extends ReadUserDto {
  private List<ReadPackageDto> packages;

  private Set<ReadCategoryDto> offerCategories;

  private List<PriorityScoreDto> priorityScores;

  private String fullName;

  private String citizenId;

  private List<String> citizenPaper;

  private Boolean editorChoice;

  private List<String> hashTag;
}
