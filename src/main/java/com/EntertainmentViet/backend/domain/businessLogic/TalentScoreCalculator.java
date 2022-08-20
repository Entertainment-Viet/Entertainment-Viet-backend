package com.EntertainmentViet.backend.domain.businessLogic;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.util.Iterator;
import java.util.Map;
import java.util.OptionalDouble;

@UtilityClass
@Slf4j
public class TalentScoreCalculator {

  /**
   * This will calculate the score with dynamic setting
   * The score is calculated by the sum of product
   *
   * Here is the sample of score metrics
   * [{"a": 3, "alpha": 0.45}, {"b": 7, "beta": 0.17}, {"c": 2, "gamma": 0.8}, {"d": 15, "delta": 0.32}]
   *
   * @param scoreData the json data for scoring metrics
   * @return score in OptionalDouble value
   */
  public OptionalDouble calculateScore(JsonNode scoreData) {
    if (!scoreData.isArray()) {
      log.error("Wrong format of scoreData, expecting an ArrayNode");
      return OptionalDouble.empty();
    }

    Double finalScore = 0.0;
    for (JsonNode data : scoreData) {
      if (data.size() != 2) {
        log.error("Wrong format of score data metrics, expecting 2 for each score metric");
        return OptionalDouble.empty();
      }

      Double productOfPara = 1.0;
      Iterator<Map.Entry<String, JsonNode>> fieldsIterator = data.fields();
      while (fieldsIterator.hasNext()) {
        Map.Entry<String, JsonNode> para = fieldsIterator.next();
        productOfPara *= para.getValue().asDouble(1.0);
      }

      finalScore += productOfPara;
    }

    return OptionalDouble.of(finalScore);
  }
}
