package com.EntertainmentViet.backend.features.talent.dto.talent;

import com.EntertainmentViet.backend.domain.entities.talent.Talent;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.OffsetDateTime;
import java.util.UUID;

@SuperBuilder
@NoArgsConstructor
@Getter
@Setter
public class ReviewDto {
  private OffsetDateTime createdAt;

  private UUID talent;

  private String comment;

  private Integer score;
}
