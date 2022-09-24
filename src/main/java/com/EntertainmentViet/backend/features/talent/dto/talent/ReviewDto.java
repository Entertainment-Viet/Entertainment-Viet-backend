package com.EntertainmentViet.backend.features.talent.dto.talent;

import com.EntertainmentViet.backend.domain.entities.talent.Talent;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.OffsetDateTime;

@SuperBuilder
@NoArgsConstructor
@Getter
@Setter
public class ReviewDto {
  private OffsetDateTime createdAt;

  private Talent talent;

  private String comment;

  private Integer score;
}
