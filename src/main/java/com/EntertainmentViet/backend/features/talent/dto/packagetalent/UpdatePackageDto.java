package com.EntertainmentViet.backend.features.talent.dto.packagetalent;

import com.EntertainmentViet.backend.domain.values.RepeatPattern;
import com.EntertainmentViet.backend.features.booking.dto.jobdetail.UpdateJobDetailDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@NoArgsConstructor
@Getter
@Setter
public class UpdatePackageDto {

  private String name;

  private Boolean isActive;

  private UpdateJobDetailDto jobDetail;

  private RepeatPattern repeatPattern;
}
