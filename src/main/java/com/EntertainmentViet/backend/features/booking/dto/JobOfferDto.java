package com.EntertainmentViet.backend.features.booking.dto;

import com.EntertainmentViet.backend.features.common.dto.IdentifiableDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@NoArgsConstructor
@SuperBuilder
@Getter
@Setter
public class JobOfferDto extends IdentifiableDto {

  private Boolean isActive;

  private Integer quantity;

  private JobDetailDto jobDetailDto;
}
