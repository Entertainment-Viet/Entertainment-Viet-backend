package com.EntertainmentViet.backend.features.talent.dto.talent;

import com.EntertainmentViet.backend.features.booking.dto.category.ReadCategoryDto;
import com.EntertainmentViet.backend.features.common.dto.ReadUserDto;
import com.EntertainmentViet.backend.features.finance.dto.UserDealFeeRateDto;
import com.EntertainmentViet.backend.features.scoresystem.dto.ReadScoreRewardListDto;
import com.EntertainmentViet.backend.features.scoresystem.dto.ReadScoreSongListDto;
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

    private List<ReadPackageDto> packages;

    private Set<ReadCategoryDto> offerCategories;

    private List<ReadScoreSongListDto> songs;

    private List<ReadScoreRewardListDto> rewards;

    private String fullName;

    private String citizenId;

    private List<String> citizenPaper;

    private Double avgReviewRate;

    private Integer reviewCount;

    private Boolean editorChoice;

    private List<String> hashTag;

    private UserDealFeeRateDto customFeeRate;
}
