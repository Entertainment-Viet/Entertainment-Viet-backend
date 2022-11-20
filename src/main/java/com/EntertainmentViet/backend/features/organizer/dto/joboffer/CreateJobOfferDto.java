package com.EntertainmentViet.backend.features.organizer.dto.joboffer;

import javax.validation.constraints.NotNull;

import com.EntertainmentViet.backend.features.booking.dto.jobdetail.CreateJobDetailDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@NoArgsConstructor
@SuperBuilder
@Getter
@Setter
public class CreateJobOfferDto {

  @NotNull
  private String name;

  @NotNull
  private CreateJobDetailDto jobDetail;
}
