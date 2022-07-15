package com.EntertainmentViet.backend.features.organizer.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@NoArgsConstructor
@Getter
@Setter
public class OrganizerDto {

  private String address;

  private boolean temporary;
}
