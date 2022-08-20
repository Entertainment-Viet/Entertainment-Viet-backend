package com.EntertainmentViet.backend.features.talent.dto;

import com.EntertainmentViet.backend.features.booking.dto.BookingDto;
import com.EntertainmentViet.backend.features.booking.dto.JobDetailDto;
import com.EntertainmentViet.backend.features.common.dto.IdentifiableDto;
import com.EntertainmentViet.backend.features.common.dto.ShoppableDto;
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
public class PackageDto extends IdentifiableDto implements ShoppableDto {
  //TODO
  private Long id;

  private String name;

  private Boolean isActive;

  private UUID talentId;

  private JobDetailDto jobDetail;

  private List<BookingDto> orders;

}
