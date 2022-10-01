package com.EntertainmentViet.backend.features.organizer.dto.shoppingcart;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
@Getter
public class ListCartItemParamDto {

  private String workType;

  private UUID category;
}
