package com.EntertainmentViet.backend.features.scoresystem.dto;

import com.EntertainmentViet.backend.config.MappingConfig;
import com.EntertainmentViet.backend.domain.entities.talent.PriorityScore;
import com.EntertainmentViet.backend.domain.entities.talent.ScoreType;
import com.EntertainmentViet.backend.features.scoresystem.dao.ScoreTypeRepository;
import com.EntertainmentViet.backend.features.talent.dto.talent.UpdateTalentKycInfoDto;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Mapper(config = MappingConfig.class,
    uses = {
      ScoreTypeMapper.class
    })
public abstract class ScoreMapper {

  @Autowired
  private ScoreTypeRepository scoreTypeRepository;

  @BeanMapping(ignoreUnmappedSourceProperties = {"talent"})
  @Mapping(target = "scoreTypeId", source = "scoreType.uid")
  public abstract PriorityScoreDto fromModelToAdminDto(PriorityScore priorityScore);

  @Mapping(target = "talent", ignore = true)
  @Mapping(target = "scoreType", source = "scoreTypeId", qualifiedByName = "toScoreTypeModel")
  public abstract PriorityScore fromAdminDtoToModel(PriorityScoreDto dto);

  @FromModelToScoreSongListDto
  public List<ReadScoreSongListDto> fromModelToReadScoreSongListDto(List<PriorityScore> priorityScores) {
    return priorityScores.stream()
        .filter(priorityScore -> priorityScore.getScoreType().getId().equals(ScoreType.SONG_SCORE_TYPE_ID))
        .map(priorityScore -> ReadScoreSongListDto.builder()
            .achievement(priorityScore.getAchievement())
            .approved(priorityScore.getApproved())
            .proof(priorityScore.getProof().stream().findAny().orElse(null))
            .build()
        )
        .collect(Collectors.toList());
  }

  @FromModelToScoreRewardListDto
  public List<ReadScoreRewardListDto> fromModelToReadScoreRewardListDto(List<PriorityScore> priorityScores) {
    return priorityScores.stream()
        .filter(priorityScore -> !priorityScore.getScoreType().getId().equals(ScoreType.SONG_SCORE_TYPE_ID))
        .map(priorityScore -> ReadScoreRewardListDto.builder()
            .scoreTypeId(priorityScore.getScoreType().getUid())
            .scoreTypeName(priorityScore.getScoreType().getName())
            .achievement(priorityScore.getAchievement())
            .approved(priorityScore.getApproved())
            .proof(priorityScore.getProof())
            .build()
        )
        .collect(Collectors.toList());
  }

  @FromKycDtoToModel
  public List<PriorityScore> fromKycDtoToModel(UpdateTalentKycInfoDto kycInfoDto) {
    var songScoreType = scoreTypeRepository.findById(ScoreType.SONG_SCORE_TYPE_ID).get();
    var songScores = kycInfoDto.getSongs().stream()
        .map(updateScoreSongListDto -> PriorityScore.builder()
            .achievement(updateScoreSongListDto.getAchievement())
            .proof(Arrays.asList(updateScoreSongListDto.getProof()))
            .scoreType(songScoreType)
            .build()
        );

    var rewardScores = kycInfoDto.getRewards().stream()
        .map(updateScoreRewardListDto -> PriorityScore.builder()
            .scoreType(scoreTypeRepository.findByUid(updateScoreRewardListDto.getScoreTypeId()).orElse(null))
            .achievement(updateScoreRewardListDto.getAchievement())
            .proof(updateScoreRewardListDto.getProof())
            .build()
        );

    return Stream.of(songScores, rewardScores).flatMap(Function.identity()).distinct().toList();
  }

  @Named("toScoreTypeModel")
  public ScoreType toScoreTypeModel(UUID scoreTypeId) {
    return scoreTypeRepository.findById(ScoreType.SONG_SCORE_TYPE_ID).orElse(null);
  }

  @Qualifier
  @Target(ElementType.METHOD)
  @Retention(RetentionPolicy.CLASS)
  public @interface FromModelToScoreSongListDto {
  }

  @Qualifier
  @Target(ElementType.METHOD)
  @Retention(RetentionPolicy.CLASS)
  public @interface FromModelToScoreRewardListDto {
  }

  @Qualifier
  @Target(ElementType.METHOD)
  @Retention(RetentionPolicy.CLASS)
  public @interface FromKycDtoToModel {
  }

}
