package com.EntertainmentViet.backend.features.talent.dto.talent;

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
public class CreatedTalentDto {

  @NotNull
  private String username;

  @NotNull
  private String password;

  @NotNull
  private String email;

  private List<String> hashTag;
}
