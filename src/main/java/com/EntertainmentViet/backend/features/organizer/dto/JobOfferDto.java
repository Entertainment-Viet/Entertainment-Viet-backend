package com.EntertainmentViet.backend.features.organizer.dto;

import com.EntertainmentViet.backend.features.booking.dto.jobdetail.JobDetailDto;
import com.EntertainmentViet.backend.features.common.dto.IdentifiableDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@NoArgsConstructor
@SuperBuilder
@Getter
@Setter
public class JobOfferDto extends IdentifiableDto {

  private String name;

  private Boolean isActive;

  private Integer quantity;

  private JobDetailDto jobDetail;

  private UUID organizerId;
}
