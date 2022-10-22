package com.EntertainmentViet.backend.features.organizer.dto.event;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.OffsetDateTime;
import java.util.List;

@NoArgsConstructor
@SuperBuilder
@Getter
@Setter
public class CreateEventDto {

  @NonNull
  private String name;

  @NonNull
  private Boolean isActive;

  @NonNull
  private String occurrenceAddress;

  @NonNull
  private OffsetDateTime occurrenceStartTime;

  @NonNull
  private OffsetDateTime occurrenceEndTime;

  private String description;

  private List<String> legalPaper;
}
