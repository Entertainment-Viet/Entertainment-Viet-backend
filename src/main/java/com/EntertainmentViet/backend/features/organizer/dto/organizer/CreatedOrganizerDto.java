package com.EntertainmentViet.backend.features.organizer.dto.organizer;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotNull;

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
  private String displayName;

  private String phoneNumber;

  @NotNull
  private String email;

  private String address;

  private String bio;

  private String extensions;
}
