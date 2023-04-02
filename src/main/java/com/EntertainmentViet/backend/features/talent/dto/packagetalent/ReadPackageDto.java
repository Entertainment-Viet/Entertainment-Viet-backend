package com.EntertainmentViet.backend.features.talent.dto.packagetalent;

import com.EntertainmentViet.backend.domain.values.RepeatPattern;
import com.EntertainmentViet.backend.features.booking.dto.jobdetail.ReadJobDetailDto;
import com.EntertainmentViet.backend.features.common.dto.IdentifiableDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@SuperBuilder
@NoArgsConstructor
@Getter
@Setter
public class ReadPackageDto extends IdentifiableDto {

  private String name;

  private Boolean isActive;

  private UUID talentId;

  private String talentName;

  private ReadJobDetailDto jobDetail;

  private Integer orderNum;

  private String packageType;

  private RepeatPattern repeatPattern;
}
