package com.EntertainmentViet.backend.features.organizer.dto.event;

import com.EntertainmentViet.backend.features.booking.dto.location.ReadLocationDto;
import com.EntertainmentViet.backend.features.common.dto.IdentifiableDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@SuperBuilder
@Getter
@Setter
public class ReadEventDto extends IdentifiableDto {

  private String name;

  private Boolean isActive;

  private ReadLocationDto occurrenceAddress;

  private OffsetDateTime occurrenceStartTime;

  private OffsetDateTime occurrenceEndTime;

  private UUID organizerId;

  private String organizerName;

  private String organizerAvatar;

  private String description;

  private List<String> legalPaper;

  private Integer openPositionsCount;

  private List<String> descriptionImg;

  private List<String> hashTag;
}
