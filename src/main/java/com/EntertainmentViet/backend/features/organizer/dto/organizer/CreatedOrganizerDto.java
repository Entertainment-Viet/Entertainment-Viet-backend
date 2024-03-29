package com.EntertainmentViet.backend.features.organizer.dto.organizer;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotNull;
import java.util.List;

@NoArgsConstructor
@SuperBuilder
@Getter
@Setter
public class CreatedOrganizerDto {

  @NotNull
  private String username;

  @NotNull
  private String password;

  @NotNull
  private String email;

  private List<String> hashTag;
}
