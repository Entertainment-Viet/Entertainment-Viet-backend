package com.EntertainmentViet.backend.features.notification.boundary;

import com.EntertainmentViet.backend.domain.entities.booking.Booking;

import java.util.UUID;

public interface BookingNotifyBoundary {

  void sendCreateNotification(UUID sender, UUID receiver, Booking booking);
  void sendUpdateNotification(UUID sender, UUID receiver, Booking booking);
  void sendAcceptNotification(UUID sender, UUID receiver, Booking booking);
  void sendRejectNotification(UUID sender, UUID receiver, Booking booking);
  void sendCancelNotification(UUID sender, UUID receiver, Booking booking);
  void sendFinishNotification(UUID sender, UUID receiver, Booking booking);

}
