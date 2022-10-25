package com.EntertainmentViet.backend.features.organizer.dto.organizer;

import com.EntertainmentViet.backend.features.common.dto.UpdateUserKycInfoDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@SuperBuilder
@NoArgsConstructor
@Getter
@Setter
public class UpdateOrganizerKycInfoDto extends UpdateUserKycInfoDto {

  private String companyName;

  private String representative;

  private String position;

  private List<String> businessPaper;
}
