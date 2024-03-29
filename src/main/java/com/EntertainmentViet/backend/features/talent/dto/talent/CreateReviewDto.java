package com.EntertainmentViet.backend.features.talent.dto.talent;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

@SuperBuilder
@NoArgsConstructor
@Getter
@Setter
public class CreateReviewDto {

  @NotNull
  private UUID organizerId;

  @NotNull
  private UUID talentId;

  private String comment;

  @Min(1)
  @Max(5)
  private Integer score;

  private List<String> reviewImg;
}
