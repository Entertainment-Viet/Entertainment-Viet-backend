package com.EntertainmentViet.backend.features.organizer.dto.event;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

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
  private UUID occurrenceAddress;

  @NonNull
  private OffsetDateTime occurrenceStartTime;

  @NonNull
  private OffsetDateTime occurrenceEndTime;

  private String description;

  private List<String> legalPaper;
}
