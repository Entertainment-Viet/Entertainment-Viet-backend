package com.EntertainmentViet.backend.features.talent.dto;

import com.EntertainmentViet.backend.domain.entities.talent.Talent;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.Instant;

@SuperBuilder
@NoArgsConstructor
@Getter
@Setter
public class ReviewDto {
  private Instant createdAt;

  private Talent talent;

  private String comment;

  private Integer score;
}
