package com.EntertainmentViet.backend.features.booking.dto.jobdetail;

import java.time.OffsetDateTime;
import java.util.UUID;

import com.EntertainmentViet.backend.features.booking.dto.location.InputLocationDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@NoArgsConstructor
@SuperBuilder
@Getter
@Setter
public class UpdateJobDetailDto {

  private UUID categoryId;

  private String workType;

  private PriceDto price;

  private OffsetDateTime performanceStartTime;

  private OffsetDateTime performanceEndTime;

  private Integer performanceCount;

  private InputLocationDto location;

  private String note;

  private String extensions;
}
