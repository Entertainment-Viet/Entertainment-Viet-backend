package com.EntertainmentViet.backend.features.talent.dto;

import com.EntertainmentViet.backend.features.booking.dto.booking.ReadBookingDto;
import com.EntertainmentViet.backend.features.booking.dto.jobdetail.ReadJobDetailDto;
import com.EntertainmentViet.backend.features.common.dto.IdentifiableDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.UUID;

@SuperBuilder
@NoArgsConstructor
@Getter
@Setter
public class PackageDto extends IdentifiableDto {

  private String name;

  private Boolean isActive;

  private UUID talentId;

  private ReadJobDetailDto jobDetail;

  private List<ReadBookingDto> orders;

}
