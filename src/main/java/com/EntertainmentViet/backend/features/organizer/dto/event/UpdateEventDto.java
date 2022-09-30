package com.EntertainmentViet.backend.features.organizer.dto.event;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.OffsetDateTime;

@NoArgsConstructor
@SuperBuilder
@Getter
@Setter
public class UpdateEventDto {

  private String name;

  private Boolean isActive;

  private String occurrenceAddress;

  private OffsetDateTime occurrenceTime;
}
