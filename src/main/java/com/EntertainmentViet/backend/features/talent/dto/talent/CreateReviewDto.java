package com.EntertainmentViet.backend.features.talent.dto.talent;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@SuperBuilder
@NoArgsConstructor
@Getter
@Setter
public class CreateReviewDto {

  @NotNull
  private UUID organizer;

  @NotNull
  private UUID talent;

  private String comment;

  @Min(1)
  @Max(5)
  private Integer score;
}
