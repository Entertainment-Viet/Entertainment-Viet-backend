package com.EntertainmentViet.backend.features.organizer.dto.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.OffsetDateTime;

@AllArgsConstructor
@Getter
public class ListEventParamDto {

  private String name;

  private Boolean isActive;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime startTime;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime endTime;

  // organizer name
  private String organizer;
}
