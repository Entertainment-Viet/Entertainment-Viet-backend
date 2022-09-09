package com.EntertainmentViet.backend.features.booking.dto.jobdetail;

import com.EntertainmentViet.backend.features.booking.dto.category.CategoryDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.Instant;
import java.util.UUID;

@NoArgsConstructor
@SuperBuilder
@Getter
@Setter
public class CreateJobDetailDto {

  private UUID categoryUid;

  private String workType;

  private PriceDto price;

  private Long performanceDuration;

  private Instant performanceTime;

  private String location;

  private String note;

  private String extensions;
}
