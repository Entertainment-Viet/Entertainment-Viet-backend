package com.EntertainmentViet.backend.features.organizer.dto.organizer;

import com.EntertainmentViet.backend.features.booking.dto.booking.ReadBookingDto;
import com.EntertainmentViet.backend.features.common.dto.ReadUserDto;
import com.EntertainmentViet.backend.features.finance.dto.UserDealFeeRateDto;
import com.EntertainmentViet.backend.features.organizer.dto.event.ReadEventDto;
import com.EntertainmentViet.backend.features.organizer.dto.joboffer.ReadJobOfferDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@SuperBuilder
@NoArgsConstructor
@Getter
@Setter
public class ReadOrganizerDto extends ReadUserDto {

  private List<ReadJobOfferDto> jobOffers;

  private List<ReadEventDto> events;

  private List<ReadBookingDto> bookings;

  private String companyName;

  private String representative;

  private String position;

  private List<String> businessPaper;

  private List<String> hashTag;

  private UserDealFeeRateDto customFeeRate;
}
