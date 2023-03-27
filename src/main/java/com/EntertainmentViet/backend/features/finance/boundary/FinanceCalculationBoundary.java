package com.EntertainmentViet.backend.features.finance.boundary;

import com.EntertainmentViet.backend.domain.entities.booking.Booking;
import com.EntertainmentViet.backend.domain.values.FinanceReport;

import java.util.Collection;

public interface FinanceCalculationBoundary {
  FinanceReport exportTalentBookingReport(Collection<Booking> bookings, boolean containInCompleted);
  FinanceReport exportOrganizerBookingReport(Collection<Booking> bookings, boolean containInCompleted);
}
