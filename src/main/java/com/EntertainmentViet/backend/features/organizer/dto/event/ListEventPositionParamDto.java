package com.EntertainmentViet.backend.features.organizer.dto.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
@Getter
public class ListEventPositionParamDto {

  // Event name
  private String event;

  private Boolean isActive;

  private Integer quantity;

  // category name
  private UUID category;

  private String workType;

  /**
   * It is false by default, it lists out events that from non-archived event, event's organizer
   */
  private Boolean withArchived = Boolean.FALSE;
}
