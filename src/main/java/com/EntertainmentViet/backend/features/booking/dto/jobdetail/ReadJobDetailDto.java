package com.EntertainmentViet.backend.features.booking.dto.jobdetail;

import com.EntertainmentViet.backend.features.booking.dto.category.CategoryDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.Instant;

@NoArgsConstructor
@SuperBuilder
@Getter
@Setter
public class ReadJobDetailDto {

  private CategoryDto category;

  private String workType;

  private PriceDto price;

  private Long performanceDuration;

  private Instant performanceTime;

  private String location;

  private String note;

  private String extensions;
}
