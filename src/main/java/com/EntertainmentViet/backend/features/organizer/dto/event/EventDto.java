package com.EntertainmentViet.backend.features.organizer.dto.event;

import com.EntertainmentViet.backend.features.common.dto.IdentifiableDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@SuperBuilder
@Getter
@Setter
public class EventDto extends IdentifiableDto {

  private String name;

  private Boolean isActive;

  private String occurrenceAddress;

  private Instant occurrenceTime;

  private UUID organizerId;

  private List<EventOpenPositionDto> openPositions;
}
