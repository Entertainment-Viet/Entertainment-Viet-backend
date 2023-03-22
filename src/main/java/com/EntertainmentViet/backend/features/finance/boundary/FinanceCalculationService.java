package com.EntertainmentViet.backend.features.finance.boundary;

import com.EntertainmentViet.backend.domain.businessLogic.FinanceLogic;
import com.EntertainmentViet.backend.domain.entities.booking.Booking;
import com.EntertainmentViet.backend.domain.entities.finance.FinanceConfig;
import com.EntertainmentViet.backend.domain.standardTypes.BookingStatus;
import com.EntertainmentViet.backend.domain.values.FinanceReport;
import com.EntertainmentViet.backend.features.config.boundary.ConfigBoundary;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Collection;

@Service
@RequiredArgsConstructor
public class FinanceCalculationService implements FinanceCalculationBoundary {

  private final ConfigBoundary configService;

  private final UserDealFeeRateBoundary userDealFeeRateService;

  private FinanceConfig financeConfig;

  @PostConstruct
  private void postConstruct() {
    financeConfig = configService.getFinance().orElse(FinanceConfig.builder().build());
  }

  @Override
  public FinanceReport exportTalentBookingReport(Collection<Booking> bookings, boolean containInCompleted) {
    return bookings.stream().filter(booking -> containInCompleted || booking.getStatus().equals(BookingStatus.FINISHED))
        .reduce(FinanceReport.builder().build(),
            (accumReport, booking) -> {
          var config = userDealFeeRateService.updateConfigOnTalentDealFee(booking.getTalent().getId(), financeConfig.clone());
          return FinanceLogic.generateTalentBookingReport(booking, config, accumReport);
        }, FinanceReport::combineWith);
  }

  @Override
  public FinanceReport exportOrganizerBookingReport(Collection<Booking> bookings, boolean containInCompleted) {
    return bookings.stream().filter(booking -> containInCompleted || booking.getStatus().equals(BookingStatus.FINISHED))
        .reduce(FinanceReport.builder().build(),
            (accumReport, booking) -> {
              var config = userDealFeeRateService.updateConfigOnOrganizerDealFee(booking.getOrganizer().getId(), financeConfig.clone());
              return FinanceLogic.generateOrganizerBookingReport(booking, config, accumReport);
            }, FinanceReport::combineWith);
  }
}
