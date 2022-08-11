package com.EntertainmentViet.backend.features.common.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

@UtilityClass
@Slf4j
public class JsonUtils {

  private final ObjectMapper mapper = new ObjectMapper();

  public Map<String, Object> jsonToMap(String jsonString) {
    try {
      return mapper.readValue(jsonString, HashMap.class);
    } catch (JsonProcessingException e) {
      log.error(String.format("Can not deserialize json: %s", jsonString));
    }
    return new HashMap<>();
  }

  public String mapToJson(Map<String, Object> jsonMap) {
    try {
      return mapper.writeValueAsString(jsonMap);
    } catch (JsonProcessingException e) {
      log.error("Can not serialize to json ", e);
    }
    return "";
  }
}
