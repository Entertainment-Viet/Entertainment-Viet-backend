package com.EntertainmentViet.backend.features.booking.dto.category;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@NoArgsConstructor
@SuperBuilder
@Getter
@Setter
public class CreateCategoryDto {

  private String name;

  private UUID parentUid;
}
