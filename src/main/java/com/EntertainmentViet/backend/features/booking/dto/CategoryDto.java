package com.EntertainmentViet.backend.features.booking.dto;

import com.EntertainmentViet.backend.features.common.dto.IdentifiableDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@NoArgsConstructor
@SuperBuilder
@Getter
@Setter
public class CategoryDto extends IdentifiableDto {

  private String name;

  private String parentName;
}
