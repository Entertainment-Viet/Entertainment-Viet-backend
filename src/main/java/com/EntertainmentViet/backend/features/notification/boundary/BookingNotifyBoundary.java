package com.EntertainmentViet.backend.features.notification.boundary;

import com.EntertainmentViet.backend.domain.entities.booking.Booking;
import com.EntertainmentViet.backend.domain.entities.notification.BookingNotification;
import com.EntertainmentViet.backend.domain.values.RepeatPattern;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface BookingNotifyBoundary {

  void sendCreateNotification(UUID receiverUid, String receiverName, Booking booking);
  void sendCreateRepeatNotification(UUID receiverUid, String receiverName, Booking booking, RepeatPattern repeatPattern);
  void sendUpdateNotification(UUID receiverUid, String receiverName, Booking booking);
  void sendAcceptNotification(UUID receiverUid, String receiverName, Booking booking);
  void sendRejectNotification(UUID receiverUid, String receiverName, Booking booking);
  void sendCancelNotification(UUID receiverUid, String receiverName, Booking booking);
  void sendFinishNotification(UUID receiverUid, String receiverName, Booking booking);
  void sendNotification(UUID receiver, BookingNotification notification);

  Page<BookingNotification> getAllNotification(UUID actorUid, Pageable pageable);
  Integer countUnreadNotification(UUID actorUid);
  void updateIsRead(UUID actorUid, boolean isRead);
}
