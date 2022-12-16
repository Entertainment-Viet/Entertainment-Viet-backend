package com.EntertainmentViet.backend.domain.businessLogic;

import com.EntertainmentViet.backend.domain.entities.booking.Booking;
import com.EntertainmentViet.backend.domain.standardTypes.AccountType;
import com.EntertainmentViet.backend.domain.standardTypes.BookingStatus;
import com.EntertainmentViet.backend.domain.values.FinanceReport;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;

@UtilityClass
@Slf4j
public class FinanceUtils {

  public static Double CORPORATION_TAX = 0.08;

  public static Double INDIVIDUAL_TAX = 0.1;

  public FinanceReport generateOrganizerBookingReport(Collection<Booking> bookings) {
    Double unpaid = 0.0;
    Double price = 0.0;
    Double tax = 0.0;

    for (Booking booking : bookings) {
      if (booking.getStatus().equals(BookingStatus.FINISHED) && booking.getJobDetail().getPrice().checkIfFixedPrice()) {
        // TODO adding currency conversion
        var netPrice = booking.getJobDetail().getPrice().getMax();
        price += netPrice;

        if (!booking.isPaid()) {
          unpaid += price;
        }

        if (booking.getOrganizer().getAccountType().equals(AccountType.CORPORATION)) {
          tax += netPrice*CORPORATION_TAX;
        } else {
          tax += netPrice*INDIVIDUAL_TAX;
        }
      }
    }

    return FinanceReport.builder()
        .unpaid(unpaid)
        .price(price)
        .tax(tax)
        .total(price+tax)
        .build();
  }

  public FinanceReport generateTalentBookingReport(Collection<Booking> bookings) {
    Double unpaid = 0.0;
    Double price = 0.0;
    Double tax = 0.0;

    for (Booking booking : bookings) {
      if (booking.getStatus().equals(BookingStatus.FINISHED) && booking.getJobDetail().getPrice().checkIfFixedPrice()) {
        // TODO adding currency conversion
        var netPrice = booking.getJobDetail().getPrice().getMax();
        price += netPrice;

        if (!booking.isPaid()) {
          unpaid += price;
        }

        if (booking.getTalent().getAccountType().equals(AccountType.CORPORATION)) {
          tax += netPrice*CORPORATION_TAX;
        } else {
          tax += netPrice*INDIVIDUAL_TAX;
        }
      }
    }

    return FinanceReport.builder()
        .unpaid(unpaid)
        .price(price)
        .tax(tax)
        .total(price-tax)
        .build();
  }
}
