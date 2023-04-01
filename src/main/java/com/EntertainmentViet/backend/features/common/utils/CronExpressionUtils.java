package com.EntertainmentViet.backend.features.common.utils;

import com.EntertainmentViet.backend.features.common.dto.RepeatPattern;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import net.redhogs.cronparser.CasingTypeEnum;
import net.redhogs.cronparser.CronExpressionDescriptor;
import net.redhogs.cronparser.Options;
import org.springframework.scheduling.support.CronExpression;

import java.text.ParseException;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@UtilityClass
@Slf4j
public class CronExpressionUtils {

  public static DateTimeFormatter cronDateTimeFormatter;

  private static Options cronDescriptorOption;

  static {
    cronDateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy");

    cronDescriptorOption = new Options();
    cronDescriptorOption.setCasingType(CasingTypeEnum.LowerCase);
    cronDescriptorOption.setTwentyFourHourTime(true);
    cronDescriptorOption.setVerbose(true);

  }

  public List<OffsetDateTime> getAllOccurrence(RepeatPattern repeatPattern) {
    List<OffsetDateTime> occurrences = new ArrayList<>();

    if (!repeatPattern.isValid()) {
      log.error("The repeatPattern is not valid");
      return occurrences;
    }

    try {
      CronExpression cronExpression = CronExpression.parse(repeatPattern.getCronExpression());
      var nextOccur = cronExpression.next(repeatPattern.getStartPeriod());
      while (nextOccur.isBefore(repeatPattern.getEndPeriod())) {
        occurrences.add(nextOccur);
        nextOccur = cronExpression.next(nextOccur);
      }
    } catch (IllegalArgumentException e) {
      log.error(String.format("The cronExpression %s is invalid", repeatPattern.getCronExpression()));
      throw new RuntimeException(e);
    }
    return occurrences;
  }

  public String getDescription(RepeatPattern repeatPattern) {
    try {
      var cronDescription = CronExpressionDescriptor.getDescription(repeatPattern.getCronExpression(), cronDescriptorOption);
      return String.format("from %s to %s, %s",
          repeatPattern.getStartPeriod().format(cronDateTimeFormatter), repeatPattern.getEndPeriod().format(cronDateTimeFormatter), cronDescription);
    } catch (ParseException e) {
      throw new RuntimeException(e);
    }
  }
}
