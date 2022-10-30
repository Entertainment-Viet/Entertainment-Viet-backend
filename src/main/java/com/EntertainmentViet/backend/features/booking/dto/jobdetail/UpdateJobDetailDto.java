package com.EntertainmentViet.backend.features.booking.dto.jobdetail;

import com.EntertainmentViet.backend.domain.values.LocationAddress;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.OffsetDateTime;
import java.util.UUID;

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

  private LocationAddress location;

  private String note;

  private String extensions;
}
