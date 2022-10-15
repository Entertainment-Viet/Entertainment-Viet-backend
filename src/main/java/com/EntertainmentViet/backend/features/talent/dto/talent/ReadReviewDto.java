package com.EntertainmentViet.backend.features.talent.dto.talent;

import com.EntertainmentViet.backend.features.common.dto.IdentifiableDto;
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
public class ReadReviewDto extends IdentifiableDto {
  private OffsetDateTime createdAt;

  private UUID talentId;

  private String talentName;

  private UUID organizerId;

  private String organizerName;

  private String comment;

  private Integer score;
}
