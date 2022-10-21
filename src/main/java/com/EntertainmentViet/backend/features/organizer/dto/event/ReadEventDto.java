package com.EntertainmentViet.backend.features.organizer.dto.event;

import com.EntertainmentViet.backend.features.common.dto.IdentifiableDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.OffsetDateTime;
import java.util.UUID;

@NoArgsConstructor
@SuperBuilder
@Getter
@Setter
public class ReadEventDto extends IdentifiableDto {

  private String name;

  private Boolean isActive;

  private String occurrenceAddress;

  private OffsetDateTime occurrenceStartTime;

  private OffsetDateTime occurrenceEndTime;

  private UUID organizerId;

  private String organizerName;
}
