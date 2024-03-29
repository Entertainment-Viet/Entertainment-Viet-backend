package com.EntertainmentViet.backend.features.booking.dto.jobdetail;

import java.time.OffsetDateTime;
import java.util.UUID;
import javax.validation.constraints.NotNull;

import com.EntertainmentViet.backend.features.booking.dto.location.InputLocationDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

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

  private InputLocationDto location;

  private String note;

  private String extensions;
}
