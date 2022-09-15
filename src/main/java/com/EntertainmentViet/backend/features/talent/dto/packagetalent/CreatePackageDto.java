package com.EntertainmentViet.backend.features.talent.dto.packagetalent;

import com.EntertainmentViet.backend.features.booking.dto.jobdetail.CreateJobDetailDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotNull;

@SuperBuilder
@NoArgsConstructor
@Getter
@Setter
public class CreatePackageDto {

  @NotNull
  private String name;

  @NotNull
  private CreateJobDetailDto jobDetail;
}
