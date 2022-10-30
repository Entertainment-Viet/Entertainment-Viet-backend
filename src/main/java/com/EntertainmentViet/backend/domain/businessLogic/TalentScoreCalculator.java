package com.EntertainmentViet.backend.domain.businessLogic;

import com.EntertainmentViet.backend.domain.values.ScoreInfo;
import com.EntertainmentViet.backend.features.admin.dto.talent.ScoreOperandDto;
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
   * @param scoreData the json data for scoring metrics
   * @return score in OptionalDouble value
   */
  public Double calculateScore(Map<String, ScoreInfo> scoreData) {
    Double finalScore = 0.0;
    for (var value : scoreData.values()) {
      if (value.getActive() != null && value.getActive()) {
        finalScore += value.getRate() * value.getMultiply();
      }
    }
    return finalScore;
  }
}
