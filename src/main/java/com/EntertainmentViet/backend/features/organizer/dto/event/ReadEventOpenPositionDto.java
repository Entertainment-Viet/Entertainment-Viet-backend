package com.EntertainmentViet.backend.features.organizer.dto.event;

import com.EntertainmentViet.backend.features.common.dto.IdentifiableDto;
import com.EntertainmentViet.backend.features.organizer.dto.joboffer.ReadJobOfferDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@NoArgsConstructor
@SuperBuilder
@Getter
@Setter
public class ReadEventOpenPositionDto extends IdentifiableDto {

  private UUID eventId;

  private ReadJobOfferDto jobOffer;

  private Integer quantity;

  private Integer applicantCount;

  private String paymentType;
}
