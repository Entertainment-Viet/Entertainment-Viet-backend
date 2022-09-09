package com.EntertainmentViet.backend.features.talent.dto;

import com.EntertainmentViet.backend.features.admin.dto.TalentFeedBackDto;
import com.EntertainmentViet.backend.features.booking.dto.booking.ReadBookingDto;
import com.EntertainmentViet.backend.features.common.dto.UserDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;


import java.util.List;

@SuperBuilder
@NoArgsConstructor
@Getter
@Setter
public class TalentDto extends UserDto {

    private List<ReviewDto> reviews;

    private List<ReadBookingDto> bookings;

    private List<TalentFeedBackDto> feedbacks;
}
