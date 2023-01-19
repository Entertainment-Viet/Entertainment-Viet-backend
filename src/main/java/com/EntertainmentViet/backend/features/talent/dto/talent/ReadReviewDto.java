package com.EntertainmentViet.backend.features.talent.dto.talent;

import com.EntertainmentViet.backend.features.common.dto.IdentifiableDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@SuperBuilder
@NoArgsConstructor
@Getter
@Setter
public class ReadReviewDto extends IdentifiableDto {
  private OffsetDateTime createdAt;

  private UUID talentId;

  private String talentName;

  private String talentAvatar;

  private UUID organizerId;

  private String organizerName;

  private String comment;

  private Integer score;

  private List<String> reviewImg;
}
