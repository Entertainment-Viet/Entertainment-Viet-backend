package com.EntertainmentViet.backend.features.booking.dto.location;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ListLocationParamDto {

	private String type;

	private Integer level;

	private UUID parentId;

}
