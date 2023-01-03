package com.EntertainmentViet.backend.features.organizer.dto.event;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@SuperBuilder
@NoArgsConstructor
@Getter
@Setter
public class CreatePositionApplicantDto {
  @NotNull
  private UUID talentId;

  @NotNull
  private String paymentType;
}
