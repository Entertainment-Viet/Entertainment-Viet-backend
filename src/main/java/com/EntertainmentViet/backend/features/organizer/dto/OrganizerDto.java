package com.EntertainmentViet.backend.features.organizer.dto;

import com.EntertainmentViet.backend.features.booking.dto.JobOfferDto;
import com.EntertainmentViet.backend.features.common.dto.UserDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@NoArgsConstructor
@Getter
@Setter
public class OrganizerDto extends UserDto {

  private JobOfferDto jobOfferDto;
}
