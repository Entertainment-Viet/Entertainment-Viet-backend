package com.EntertainmentViet.backend.features.talent.dto.talent;

import com.EntertainmentViet.backend.features.admin.dto.TalentFeedBackDto;
import com.EntertainmentViet.backend.features.booking.dto.booking.ReadBookingDto;
import com.EntertainmentViet.backend.features.booking.dto.category.CategoryDto;
import com.EntertainmentViet.backend.features.common.dto.ReadUserDto;
import com.EntertainmentViet.backend.features.talent.dto.packagetalent.ReadPackageDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;


import java.util.List;
import java.util.Set;

@SuperBuilder
@NoArgsConstructor
@Getter
@Setter
public class ReadTalentDto extends ReadUserDto {

    private List<TalentFeedBackDto> feedbacks;

    private List<ReadPackageDto> packages;

    private Set<CategoryDto> offerCategories;

    private Double finalScore;
}
