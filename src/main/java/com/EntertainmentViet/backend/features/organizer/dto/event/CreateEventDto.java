package com.EntertainmentViet.backend.features.organizer.dto.event;

import com.EntertainmentViet.backend.features.common.dto.IdentifiableDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

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
  private OffsetDateTime occurrenceTime;
}
