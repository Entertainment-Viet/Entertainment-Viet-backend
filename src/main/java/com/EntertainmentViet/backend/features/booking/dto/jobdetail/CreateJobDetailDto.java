package com.EntertainmentViet.backend.features.booking.dto.jobdetail;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.UUID;

@NoArgsConstructor
@SuperBuilder
@Getter
@Setter
public class CreateJobDetailDto {

  @NotNull
  private UUID categoryUid;

  @NotNull
  private String workType;

  @NotNull
  private PriceDto price;

  @NotNull
  private Long performanceDuration;

  @NotNull
  private Instant performanceTime;

  private String location;

  private String note;

  private String extensions;
}
