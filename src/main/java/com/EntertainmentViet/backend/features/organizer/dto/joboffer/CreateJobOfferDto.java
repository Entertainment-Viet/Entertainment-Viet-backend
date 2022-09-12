package com.EntertainmentViet.backend.features.organizer.dto.joboffer;

import com.EntertainmentViet.backend.features.booking.dto.jobdetail.CreateJobDetailDto;
import com.EntertainmentViet.backend.features.booking.dto.jobdetail.ReadJobDetailDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@NoArgsConstructor
@SuperBuilder
@Getter
@Setter
public class CreateJobOfferDto {

  private String name;

  private Integer quantity;

  private CreateJobDetailDto jobDetail;
}
