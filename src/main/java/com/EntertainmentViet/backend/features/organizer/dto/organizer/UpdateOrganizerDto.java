package com.EntertainmentViet.backend.features.organizer.dto.organizer;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@SuperBuilder
@NoArgsConstructor
@Getter
@Setter
public class UpdateOrganizerDto {

  private String displayName;

  private String avatar;

  private String bio;

  private String extensions;

  private List<String> descriptionImg;

  private List<String> hashTag;
}
