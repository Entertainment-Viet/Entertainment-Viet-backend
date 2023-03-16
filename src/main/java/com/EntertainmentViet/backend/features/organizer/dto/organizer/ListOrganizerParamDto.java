package com.EntertainmentViet.backend.features.organizer.dto.organizer;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.OffsetDateTime;
import java.util.UUID;

@AllArgsConstructor
@Getter
public class ListOrganizerParamDto {

  private String displayName;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime startTime;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime endTime;

  private UUID locationId;

  private Boolean withArchived;

  private String searchPattern;
}
