package com.EntertainmentViet.backend.features.admin.dto.talent;

import com.EntertainmentViet.backend.config.MappingConfig;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Qualifier;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Mapper(config = MappingConfig.class)
public class ScoreMapper {

  private final ObjectMapper objectMapper = new ObjectMapper();

  @FromJsonToAdminDto
  public List<ScoreOperandDto> fromJsonToAdminDto(Map<String, ScoreOperandDto> scoreSystem) {
    if (scoreSystem == null) {
      return Collections.emptyList();
    }

    return scoreSystem.entrySet().stream()
        .map(entry -> ScoreOperandDto.builder()
            .id(entry.getKey())
            .name(entry.getValue().getName())
            .rate(entry.getValue().getRate())
            .multiply(entry.getValue().getMultiply())
            .active(entry.getValue().getActive())
            .build()
        )
        .collect(Collectors.toList());
  }

  @FromJsonToTalentDto
  public List<ScoreToggleDto> fromJsonToTalentDto(Map<String, ScoreOperandDto> scoreSystem) {
    if (scoreSystem == null) {
      return Collections.emptyList();
    }
    return scoreSystem.entrySet().stream()
        .map(entry -> ScoreToggleDto.builder()
            .id(entry.getKey())
            .name(entry.getValue().getName())
            .active(entry.getValue().getActive())
            .build())
        .collect(Collectors.toList());
  }

  @FromTalentDtoToJson
  public Map<String, ScoreOperandDto> fromTalentDtoToJson(List<ScoreToggleDto> dtoList) {
    return dtoList.stream()
        .collect(Collectors.toMap(ScoreToggleDto::getId, dto -> ScoreOperandDto.builder().active(dto.getActive()).build()));
  }

  @FromAdminDtoToJson
  public Map<String, ScoreOperandDto> fromAdminDtoToJson(List<ScoreOperandDto> dtoList) {
    return dtoList.stream().collect(Collectors.toMap(ScoreOperandDto::getId, dto -> ScoreOperandDto.builder()
        .name(dto.getName())
        .rate(dto.getRate())
        .multiply(dto.getMultiply() == null ? 1 : dto.getMultiply())
        .build()
    ));
  }

  @Qualifier
  @Target(ElementType.METHOD)
  @Retention(RetentionPolicy.CLASS)
  public @interface FromJsonToAdminDto {
  }

  @Qualifier
  @Target(ElementType.METHOD)
  @Retention(RetentionPolicy.CLASS)
  public @interface FromJsonToTalentDto {
  }

  @Qualifier
  @Target(ElementType.METHOD)
  @Retention(RetentionPolicy.CLASS)
  public @interface FromTalentDtoToJson {
  }

  @Qualifier
  @Target(ElementType.METHOD)
  @Retention(RetentionPolicy.CLASS)
  public @interface FromAdminDtoToJson {
  }
}