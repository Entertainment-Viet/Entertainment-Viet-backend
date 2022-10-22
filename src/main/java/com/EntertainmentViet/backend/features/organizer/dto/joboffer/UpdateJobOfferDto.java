package com.EntertainmentViet.backend.features.organizer.dto.joboffer;

import com.EntertainmentViet.backend.features.booking.dto.jobdetail.UpdateJobDetailDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@NoArgsConstructor
@SuperBuilder
@Getter
@Setter
public class UpdateJobOfferDto {

  private String name;

  private UpdateJobDetailDto jobDetail;
}
