package com.EntertainmentViet.backend.features.common.dto;

import com.EntertainmentViet.backend.config.MappingConfig;
import com.EntertainmentViet.backend.features.common.utils.JsonUtils;
import com.fasterxml.jackson.databind.JsonNode;
import org.mapstruct.Mapper;
import org.mapstruct.Qualifier;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Mapper(config = MappingConfig.class)
public class ExtensionsMapper {

  @Qualifier
  @Target(ElementType.METHOD)
  @Retention(RetentionPolicy.CLASS)
  public @interface ToNode {
  }

  @Qualifier
  @Target(ElementType.METHOD)
  @Retention(RetentionPolicy.CLASS)
  public @interface ToJson {
  }

  @ToNode
  public JsonNode jsonToNode(String jsonString) {
    return JsonUtils.jsonToNode(jsonString);
  }

  @ToJson
  public String mapToJson(JsonNode node) {
    return JsonUtils.nodeToJson(node);
  }
}
