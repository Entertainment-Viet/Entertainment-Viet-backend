package com.EntertainmentViet.backend.features.booking.dto;

import com.EntertainmentViet.backend.features.common.dto.IdentifiableDto;

public class JobOfferDto extends IdentifiableDto {

  private Boolean isActive;

  private Integer quantity;

  private JobDetailDto jobDetailDto;
}
