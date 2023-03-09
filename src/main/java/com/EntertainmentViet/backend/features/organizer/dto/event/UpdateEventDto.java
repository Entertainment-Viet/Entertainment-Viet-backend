package com.EntertainmentViet.backend.features.organizer.dto.event;

import com.EntertainmentViet.backend.features.booking.dto.location.InputLocationDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.OffsetDateTime;
import java.util.List;

@NoArgsConstructor
@SuperBuilder
@Getter
@Setter
public class UpdateEventDto {

  private String name;

  private Boolean isActive;

  private InputLocationDto occurrenceAddress;

  private OffsetDateTime occurrenceStartTime;

  private OffsetDateTime occurrenceEndTime;

  private String description;

  private List<String> legalPaper;

  private List<String> descriptionImg;

  private List<String> hashTag;
}
