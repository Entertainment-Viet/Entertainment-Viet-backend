package com.EntertainmentViet.backend.features.talent.dto.talent;

import com.EntertainmentViet.backend.features.common.dto.CustomPage;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@NoArgsConstructor
@SuperBuilder
@Getter
@Setter
public class ReviewResponseDto {
  private Integer sumScore1;
  private Integer sumScore2;
  private Integer sumScore3;
  private Integer sumScore4;
  private Integer sumScore5;

  private CustomPage<ReadReviewDto> reviews;
}
