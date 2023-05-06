package com.EntertainmentViet.backend.domain.values;

import com.EntertainmentViet.backend.domain.businessLogic.CronExpressionLogic;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.scheduling.support.CronExpression;

import javax.persistence.Embeddable;
import java.time.OffsetDateTime;
import java.util.List;

@Embeddable
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class RepeatPattern {

  /**
   * The expression is in format "second minute hour day-of-the-month month day-of-the-week
   * E.g.: 0 0 12 * * ?
   */
  private String cronExpression;

  private OffsetDateTime startPeriod;

  private OffsetDateTime endPeriod;

  @Schema(hidden = true)
  public boolean isValid() {
    return cronExpression != null && startPeriod != null && endPeriod != null && CronExpression.isValidExpression(cronExpression);
  }

  @Schema(hidden = true)
  public List<OffsetDateTime> getAllOccurrence() {
    return CronExpressionLogic.getAllOccurrence(this);
  }

  @Schema(hidden = true)
  public String getDescription() {
    return CronExpressionLogic.getDescription(this);
  }
}
