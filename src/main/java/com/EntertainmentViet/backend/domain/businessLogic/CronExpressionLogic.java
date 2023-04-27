package com.EntertainmentViet.backend.domain.businessLogic;

import com.EntertainmentViet.backend.domain.entities.booking.Booking;
import com.EntertainmentViet.backend.domain.values.RepeatPattern;
import com.EntertainmentViet.backend.exception.rest.InconsistentEntityStateException;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import net.redhogs.cronparser.CasingTypeEnum;
import net.redhogs.cronparser.CronExpressionDescriptor;
import net.redhogs.cronparser.Options;
import org.springframework.scheduling.support.CronExpression;

import java.text.ParseException;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@UtilityClass
@Slf4j
public class CronExpressionLogic {

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
      throw new InconsistentEntityStateException("The repeatPattern is not valid");
    }

    try {
      CronExpression cronExpression = CronExpression.parse(repeatPattern.getCronExpression());
      var nextOccur = cronExpression.next(repeatPattern.getStartPeriod());
      while (nextOccur.isBefore(repeatPattern.getEndPeriod())) {
        occurrences.add(nextOccur);
        nextOccur = cronExpression.next(nextOccur);
      }
    } catch (IllegalArgumentException e) {
      throw new InconsistentEntityStateException(String.format("The cronExpression %s is invalid", repeatPattern.getCronExpression()));
    }
    return occurrences;
  }

  public String getDescription(RepeatPattern repeatPattern) {
    try {
      var cronDescription = CronExpressionDescriptor.getDescription(repeatPattern.getCronExpression(), cronDescriptorOption);
      return String.format("from %s to %s, %s",
          repeatPattern.getStartPeriod().format(cronDateTimeFormatter), repeatPattern.getEndPeriod().format(cronDateTimeFormatter), cronDescription);
    } catch (ParseException e) {
      throw new InconsistentEntityStateException("Can not get parse repeatPattern: "+repeatPattern.getCronExpression());
    }
  }

  public List<Booking> generateBookingList(Booking template, RepeatPattern repeatPattern) {
    List<Booking> createdBooking = new ArrayList<>();
    Duration performanceDuration = Duration.between(template.getJobDetail().getPerformanceEndTime(), template.getJobDetail().getPerformanceStartTime());

    var occurrences = repeatPattern.getAllOccurrence();
    for (var occur : occurrences) {
      Booking repeatBooking = template.clone();
      repeatBooking.getJobDetail().setPerformanceStartTime(occur);
      repeatBooking.getJobDetail().setPerformanceEndTime(occur.plus(performanceDuration));

      createdBooking.add(repeatBooking);
    }
    return createdBooking;
  }
}
