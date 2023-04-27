package com.EntertainmentViet.backend.features.common.utils;

import com.EntertainmentViet.backend.exception.rest.InvalidInputException;
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
    if (jsonString == null) {
      return null;
    }
    try {
      return mapper.readTree(jsonString);
    } catch (JsonProcessingException e) {
      throw new InvalidInputException(String.format("Can not deserialize json: %s", jsonString));
    }
  }

  public String nodeToJson(JsonNode node) {
    return node != null ? node.toString() : "";
  }
}
