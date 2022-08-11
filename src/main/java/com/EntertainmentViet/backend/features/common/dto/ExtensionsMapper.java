package com.EntertainmentViet.backend.features.common.dto;

import com.EntertainmentViet.backend.config.MappingConfig;
import com.EntertainmentViet.backend.features.common.utils.JsonUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Qualifier;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Map;

@Mapper(config = MappingConfig.class)
public class ExtensionsMapper {

  @Qualifier
  @Target(ElementType.METHOD)
  @Retention(RetentionPolicy.CLASS)
  public @interface ToMap {
  }

  @Qualifier
  @Target(ElementType.METHOD)
  @Retention(RetentionPolicy.CLASS)
  public @interface ToJson {
  }

  @ToMap
  public Map<String, Object> jsonToMap(String jsonString) {
    return JsonUtils.jsonToMap(jsonString);
  }

  @ToJson
  public String mapToJson(Map<String, Object> map) {
    return JsonUtils.mapToJson(map);
  }
}
