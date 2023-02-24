package com.EntertainmentViet.backend.features.notification.boundary;

import com.EntertainmentViet.backend.domain.entities.booking.Booking;
import com.EntertainmentViet.backend.domain.entities.notification.BookingNotification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface BookingNotifyBoundary {

  void sendCreateNotification(UUID receiver, Booking booking);
  void sendUpdateNotification(UUID receiver, Booking booking);
  void sendAcceptNotification(UUID receiver, Booking booking);
  void sendRejectNotification(UUID receiver, Booking booking);
  void sendCancelNotification(UUID receiver, Booking booking);
  void sendFinishNotification(UUID receiver, Booking booking);
  void sendNotification(UUID receiver, BookingNotification notification);

  Page<BookingNotification> getAllNotification(UUID actorUid, Pageable pageable);
  void updateIsRead(UUID actorUid, boolean isRead);
}
