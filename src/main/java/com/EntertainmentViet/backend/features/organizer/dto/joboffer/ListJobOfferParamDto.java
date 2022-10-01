package com.EntertainmentViet.backend.features.organizer.dto.joboffer;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
@Getter
public class ListJobOfferParamDto {
  private String name;
  private Boolean isActive;

  // organizer name
  private String organizer;

  private UUID category;
}
