package com.EntertainmentViet.backend.domain.businessLogic;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.experimental.UtilityClass;

import java.util.OptionalDouble;

@UtilityClass
public class TalentScoreCalculator {

  public OptionalDouble calculateScore(JsonNode scoreData) {
    return OptionalDouble.empty();
  }
}
