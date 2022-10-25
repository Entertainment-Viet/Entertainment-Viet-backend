package com.EntertainmentViet.backend.features.organizer.dto.organizer;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@NoArgsConstructor
@Getter
@Setter
public class UpdateOrganizerDto {

  private String displayName;

  private String bio;

  private String extensions;
}
