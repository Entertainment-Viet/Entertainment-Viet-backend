/*
 * Author : AdNovum Informatik AG
 */

package com.EntertainmentViet.backend.features.booking.dto.booking;

import java.time.OffsetDateTime;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@NoArgsConstructor
@Getter
@Setter
public class ReadOtherBookingDto {

	private OffsetDateTime performanceStartTime;

	private OffsetDateTime performanceEndTime;
}
