package com.EntertainmentViet.backend.features.security.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
public class CreatedKeycloakUserDto {
  @Builder.Default
  private Boolean enabled = true;

  private String firstName;

  private String lastName;

  private String email;

  private String username;

  private List<CredentialDto> credentials;

  private List<String> groups;

  @NoArgsConstructor
  @Getter
  @Setter
  @SuperBuilder
  public static class CredentialDto {
    @Builder.Default
    private String type = "password";

    @Builder.Default
    private Boolean temporary = false;

    private String value;
  }
}
