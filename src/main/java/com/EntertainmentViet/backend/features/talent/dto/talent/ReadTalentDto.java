package com.EntertainmentViet.backend.features.talent.dto.talent;

import com.EntertainmentViet.backend.features.admin.dto.TalentFeedBackDto;
import com.EntertainmentViet.backend.features.booking.dto.booking.ReadBookingDto;
import com.EntertainmentViet.backend.features.common.dto.ReadUserDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;


import java.util.List;

@SuperBuilder
@NoArgsConstructor
@Getter
@Setter
public class ReadTalentDto extends ReadUserDto {

    private List<ReviewDto> reviews;

    private List<TalentFeedBackDto> feedbacks;
}
