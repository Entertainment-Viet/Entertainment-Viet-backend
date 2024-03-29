package com.EntertainmentViet.backend.features.talent.dto.talent;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@SuperBuilder
@Getter
@Setter
public class UpdateTalentDto {

  private String displayName;

  private String avatar;

  private String bio;

  private List<UUID> offerCategories;

  private String extensions;

  private List<String> descriptionImg;

  private Boolean editorChoice;

  private List<String> hashTag;
}
