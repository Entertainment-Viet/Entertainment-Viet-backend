package com.EntertainmentViet.backend.features.talent.dto.talent;

import com.EntertainmentViet.backend.features.common.dto.UpdateUserKycInfoDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@NoArgsConstructor
@SuperBuilder
@Getter
@Setter
public class UpdateTalentKycInfoDto extends UpdateUserKycInfoDto {

  private String lastName;

  private String firstName;

  private String citizenId;

  private List<String> citizenPaper;
}
