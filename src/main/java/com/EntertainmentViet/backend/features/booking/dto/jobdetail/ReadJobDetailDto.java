package com.EntertainmentViet.backend.features.booking.dto.jobdetail;

import com.EntertainmentViet.backend.features.booking.dto.category.CategoryDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotNull;
import java.time.Instant;

@NoArgsConstructor
@SuperBuilder
@Getter
@Setter
public class ReadJobDetailDto {

  private CategoryDto category;

  private String workType;

  private PriceDto price;

  private Instant performanceStartTime;

  private Instant performanceEndTime;

  private Integer performanceCount;

  private String location;

  private String note;

  private String extensions;
}
