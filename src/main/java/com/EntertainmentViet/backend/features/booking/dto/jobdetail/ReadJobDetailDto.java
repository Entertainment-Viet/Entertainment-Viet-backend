package com.EntertainmentViet.backend.features.booking.dto.jobdetail;

import com.EntertainmentViet.backend.features.booking.dto.category.ReadCategoryDto;
import com.EntertainmentViet.backend.features.booking.dto.location.ReadLocationDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.OffsetDateTime;

@NoArgsConstructor
@SuperBuilder
@Getter
@Setter
public class ReadJobDetailDto {

  private ReadCategoryDto category;

  private String workType;

  private PriceDto price;

  private OffsetDateTime performanceStartTime;

  private OffsetDateTime performanceEndTime;

  private Integer performanceCount;

  private ReadLocationDto location;

  private String note;

  private String extensions;
}
