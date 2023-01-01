package com.EntertainmentViet.backend.domain.businessLogic;

import com.EntertainmentViet.backend.domain.entities.booking.Booking;
import com.EntertainmentViet.backend.domain.entities.booking.JobDetail;
import com.EntertainmentViet.backend.domain.entities.config.FinanceConfig;
import com.EntertainmentViet.backend.domain.standardTypes.BookingStatus;
import com.EntertainmentViet.backend.domain.standardTypes.UserType;
import com.EntertainmentViet.backend.domain.values.FinanceReport;
import com.EntertainmentViet.backend.domain.values.Price;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.function.Predicate;

@UtilityClass
@Slf4j
public class FinanceLogic {

  public Double calculateUnpaidSum(Collection<Booking> bookings) {
    return bookings.stream()
        .filter(booking -> booking.getStatus().equals(BookingStatus.FINISHED))
        .filter(Predicate.not(Booking::isPaid))
        .map(Booking::getJobDetail)
        .map(JobDetail::getPrice)
        .map(Price::getMax)
        .mapToDouble(Double::doubleValue)
        .sum();
  }

  public FinanceReport generateOrganizerBookingReport(Collection<Booking> bookings, FinanceConfig config) {
    if (config == null || !config.ifValid()) {
      log.warn("Can not generate organizer finance report due to invalid config");
    }

    FinanceReport report = FinanceReport.builder().build();

    for (Booking booking : bookings) {
      if (booking.getStatus().equals(BookingStatus.FINISHED) && booking.getJobDetail().getPrice().checkIfFixedPrice()) {
        // TODO adding currency conversion
        var grossPrice = booking.getJobDetail().getPrice().getMax();

        if (booking.getOrganizer().getUserType().equals(UserType.CORPORATION)) {
          report.combineWith(buildCooperationOrganizerReport(grossPrice, config.getVat(), config.getOrganizerFee()));
        } else {
          report.combineWith(buildIndividualOrganizerReport(grossPrice, config.getVat(), config.getOrganizerFee()));
        }
      }
    }

    return report;
  }

  public FinanceReport generateTalentBookingReport(Collection<Booking> bookings, FinanceConfig config) {
    if (config == null || !config.ifValid()) {
      log.warn("Can not generate talent finance report due to invalid config");
    }

    FinanceReport report = FinanceReport.builder().build();

    for (Booking booking : bookings) {
      if (booking.getStatus().equals(BookingStatus.FINISHED) && booking.getJobDetail().getPrice().checkIfFixedPrice()) {
        // TODO adding currency conversion
        var grossPrice = booking.getJobDetail().getPrice().getMax();

        if (booking.getTalent().getUserType().equals(UserType.CORPORATION)) {
          report.combineWith(buildCooperationTalentReport(grossPrice, config.getVat(), config.getOrganizerFee()));
        } else {
          report.combineWith(buildIndividualTalentReport(grossPrice, config.getPit(), config.getOrganizerFee()));
        }
      }
    }

    return report;
  }

  private FinanceReport buildIndividualTalentReport(Double price, Double taxRate, Double feeRate) {
    var fee = price * feeRate;
    var subTotal = price - fee;
    var tax = subTotal / taxRate - subTotal;
    return FinanceReport.builder()
        .price(price)
        .fee(fee)
        .tax(tax)
        .total(subTotal)
        .build();
  }

  private FinanceReport buildCooperationTalentReport(Double price, Double taxRate, Double feeRate) {
    var fee = price * feeRate;
    var subTotal = price - fee;
    var tax = subTotal * taxRate;
    return FinanceReport.builder()
        .price(price)
        .fee(fee)
        .tax(tax)
        .total(subTotal + tax)
        .build();
  }

  private FinanceReport buildIndividualOrganizerReport(Double price, Double taxRate, Double feeRate) {
    var fee = price * feeRate;
    var subTotal = price + fee;
    var tax = subTotal * taxRate;
    return FinanceReport.builder()
        .price(price)
        .fee(fee)
        .tax(tax)
        .total(subTotal + tax)
        .build();
  }

  private FinanceReport buildCooperationOrganizerReport(Double price, Double taxRate, Double feeRate) {
    // Cooperation Organizer use same formula as Individual Organizer
    return buildIndividualOrganizerReport(price, taxRate, feeRate);
  }
}
