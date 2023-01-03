package com.EntertainmentViet.backend.features.organizer.dto.event;

import com.EntertainmentViet.backend.features.organizer.dto.joboffer.CreateJobOfferDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotNull;

@NoArgsConstructor
@SuperBuilder
@Getter
@Setter
public class CreateEventOpenPositionDto {

  @NotNull
  private CreateJobOfferDto jobOffer;

  @NotNull
  private Integer quantity;

  @NotNull
  private String paymentType;
}
