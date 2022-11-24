package com.EntertainmentViet.backend.features.booking.dto.location;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@NoArgsConstructor
@SuperBuilder
@Getter
@Setter
public class ReadLocationDto {
  private UUID uid;
  private String name;
  private String type;
  private Integer level;
  private ReadLocationDto parent;
}
