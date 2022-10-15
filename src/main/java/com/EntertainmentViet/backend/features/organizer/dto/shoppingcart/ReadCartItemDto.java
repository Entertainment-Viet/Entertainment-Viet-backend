package com.EntertainmentViet.backend.features.organizer.dto.shoppingcart;

import com.EntertainmentViet.backend.features.booking.dto.jobdetail.ReadJobDetailDto;
import com.EntertainmentViet.backend.features.common.dto.IdentifiableDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@SuperBuilder
@NoArgsConstructor
@Getter
@Setter
public class ReadCartItemDto extends IdentifiableDto {

  private String name;

  private Boolean isValid;

  private Double suggestedPrice;

  private UUID talentId;

  private String talentName;

  private ReadJobDetailDto jobDetail;
}
