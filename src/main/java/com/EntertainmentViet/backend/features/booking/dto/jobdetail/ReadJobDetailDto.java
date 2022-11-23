package com.EntertainmentViet.backend.features.booking.dto.jobdetail;

import java.time.OffsetDateTime;

import com.EntertainmentViet.backend.features.booking.dto.category.CategoryDto;
import com.EntertainmentViet.backend.features.booking.dto.location.LocationDto;
import com.EntertainmentViet.backend.features.booking.dto.location.ReadLocationDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@NoArgsConstructor
@SuperBuilder
@Getter
@Setter
public class ReadJobDetailDto {

  private CategoryDto category;

  private String workType;

  private PriceDto price;

  private OffsetDateTime performanceStartTime;

  private OffsetDateTime performanceEndTime;

  private Integer performanceCount;

  private ReadLocationDto location;

  private String note;

  private String extensions;
}
