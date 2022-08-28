package com.EntertainmentViet.backend.features.booking.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.Duration;
import java.time.Instant;

@NoArgsConstructor
@SuperBuilder
@Getter
@Setter
public class JobDetailDto {

  private CategoryDto category;

  private String workType;

  private PriceDto price;

  private Long performanceDuration;

  private Instant performanceTime;

  private String location;

  private String note;

  private String extensions;
}
