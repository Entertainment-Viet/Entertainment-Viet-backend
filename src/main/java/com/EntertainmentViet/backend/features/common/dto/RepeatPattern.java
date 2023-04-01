package com.EntertainmentViet.backend.features.common.dto;

import com.EntertainmentViet.backend.features.common.utils.CronExpressionUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@Builder(toBuilder = true)
public class RepeatPattern {

  /**
   * The expression is in format "second minute hour day-of-the-month month day-of-the-week
   * E.g.: 0 0 12 * * ?
   */
  private String cronExpression;

  private OffsetDateTime startPeriod;

  private OffsetDateTime endPeriod;

  public boolean isValid() {
    return cronExpression != null && startPeriod != null && endPeriod != null;
  }

  public List<OffsetDateTime> getAllOccurrence() {
    return CronExpressionUtils.getAllOccurrence(this);
  }

  public String getDescription() {
    return CronExpressionUtils.getDescription(this);
  }
}
