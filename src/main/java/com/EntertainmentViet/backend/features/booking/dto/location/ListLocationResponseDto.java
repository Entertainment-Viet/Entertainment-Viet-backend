/*
 * Author : AdNovum Informatik AG
 */

package com.EntertainmentViet.backend.features.booking.dto.location;

import com.EntertainmentViet.backend.features.common.dto.CustomPage;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@NoArgsConstructor
@SuperBuilder
@Getter
@Setter
public class ListLocationResponseDto {

	private CustomPage<LocationDto> locationAddresses;

}
