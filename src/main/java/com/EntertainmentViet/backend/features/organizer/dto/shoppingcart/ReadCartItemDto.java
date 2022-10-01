package com.EntertainmentViet.backend.features.organizer.dto.shoppingcart;

import com.EntertainmentViet.backend.features.booking.dto.jobdetail.ReadJobDetailDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@SuperBuilder
@NoArgsConstructor
@Getter
@Setter
public class ReadCartItemDto {

  private String name;

  private Boolean isActive;

  private Double suggestedPrice;

  private UUID talentId;

  private ReadJobDetailDto jobDetail;
}
