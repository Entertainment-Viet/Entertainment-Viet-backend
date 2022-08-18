package com.EntertainmentViet.backend.features.common.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

@UtilityClass
@Slf4j
public class JsonUtils {

  private final ObjectMapper mapper = new ObjectMapper();

  public JsonNode jsonToNode(String jsonString) {
    try {
      return mapper.readTree(jsonString);
    } catch (JsonProcessingException e) {
      log.error(String.format("Can not deserialize json: %s", jsonString));
    }
    return null;
  }

  public String nodeToJson(JsonNode node) {
    return node != null ? node.toString() : "";
  }
}
