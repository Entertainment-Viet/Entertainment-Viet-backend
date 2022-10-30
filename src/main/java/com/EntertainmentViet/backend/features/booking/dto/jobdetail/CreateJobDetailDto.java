package com.EntertainmentViet.backend.features.booking.dto.jobdetail;

import com.EntertainmentViet.backend.domain.values.LocationAddress;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotNull;
import java.time.OffsetDateTime;
import java.util.UUID;

@NoArgsConstructor
@SuperBuilder
@Getter
@Setter
public class CreateJobDetailDto {

  @NotNull
  private UUID categoryId;

  @NotNull
  private String workType;

  @NotNull
  private PriceDto price;

  @NotNull
  private OffsetDateTime performanceStartTime;

  @NotNull
  private OffsetDateTime performanceEndTime;

  @NotNull
  private Integer performanceCount;

  private LocationAddress location;

  private String note;

  private String extensions;
}
