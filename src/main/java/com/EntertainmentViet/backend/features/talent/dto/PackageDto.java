package com.EntertainmentViet.backend.features.talent.dto;

import com.EntertainmentViet.backend.features.booking.dto.BookingDto;
import com.EntertainmentViet.backend.features.booking.dto.JobDetailDto;
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

  private JobDetailDto jobDetail;

  private List<BookingDto> orders;

}
