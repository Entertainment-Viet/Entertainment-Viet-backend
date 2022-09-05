package com.EntertainmentViet.backend.features.admin.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@NoArgsConstructor
@SuperBuilder
@Getter
@Setter
public class CreatedOrganizerDto {

  private String username;

  private String password; // TODO encryption this

  private String displayName;

  private String phoneNumber;

  private String email;

  private String address;

  private String bio;
}
