package com.EntertainmentViet.backend.features.notification.boundary;

import com.EntertainmentViet.backend.config.constants.AppConstant;
import com.EntertainmentViet.backend.domain.entities.booking.Booking;
import com.EntertainmentViet.backend.domain.entities.notification.BookingNotification;
import com.EntertainmentViet.backend.features.notification.dao.BookingNotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingNotifyService implements BookingNotifyBoundary {

  private final SimpMessagingTemplate messagingTemplate;

  private final BookingNotificationRepository bookingNotificationRepository;

  @Override
  public void sendCreateNotification(UUID receiver, Booking booking) {
    var sender = getSenderIdFromToken();
    sendNotificationWithContent(sender, receiver, booking,
        String.format("User #%s creates a new booking request(%s) for user #%s",
          sender.toString(), booking.getBookingCode(), receiver.toString()));
  }

  @Override
  public void sendUpdateNotification(UUID receiver, Booking booking) {
    var sender = getSenderIdFromToken();
    sendNotificationWithContent(sender, receiver, booking,
        String.format("User #%s update booking(%s)",
            sender.toString(), booking.getBookingCode()));
  }

  @Override
  public void sendAcceptNotification(UUID receiver, Booking booking) {
    var sender = getSenderIdFromToken();
    sendNotificationWithContent(sender, receiver, booking,
        String.format("User #%s accept booking request(%s)",
            sender.toString(), booking.getBookingCode()));
  }

  @Override
  public void sendRejectNotification(UUID receiver, Booking booking) {
    var sender = getSenderIdFromToken();
    sendNotificationWithContent(sender, receiver, booking,
        String.format("User #%s reject booking request(%s)",
            sender.toString(), booking.getBookingCode()));
  }

  @Override
  public void sendCancelNotification(UUID receiver, Booking booking) {
    var sender = getSenderIdFromToken();
    sendNotificationWithContent(sender, receiver, booking,
        String.format("User #%s cancel booking(%s)",
            sender.toString(), booking.getBookingCode()));
  }

  @Override
  public void sendFinishNotification(UUID receiver, Booking booking) {
    var sender = getSenderIdFromToken();
    sendNotificationWithContent(sender, receiver, booking,
        String.format("User #%s confirm booking(%s) is finish",
            sender.toString(), booking.getBookingCode()));

  }

  @Override
  public Page<BookingNotification> getAllNotification(UUID actorUid, Pageable pageable) {
    return bookingNotificationRepository.findByRecipientUidOrderByCreatedAtDesc(actorUid, pageable);
  }

  @Override
  public Integer countUnreadNotification(UUID actorUid) {
    return bookingNotificationRepository.countByRecipientUidAndIsRead(actorUid, false);
  }

  @Override
  public void updateIsRead(UUID actorUid, boolean isRead) {
    bookingNotificationRepository.updateIsRead(isRead, actorUid);
  }

  @Override
  public void sendNotification(UUID receiver, BookingNotification notification) {
    messagingTemplate.convertAndSendToUser(String.valueOf(receiver), AppConstant.NOTIFICATION_LAST_BOOKING_TOPIC, notification);
  }

  private void sendNotificationWithContent(UUID sender, UUID receiver, Booking booking, String content) {
    var notification = BookingNotification.builder()
        .senderUid(sender)
        .recipientUid(receiver)
        .bookingId(booking.getId())
        .content(content)
        .createdAt(OffsetDateTime.now())
        .isRead(false)
        .build();

    var savedNotification = bookingNotificationRepository.save(notification);
    sendNotification(receiver, savedNotification);
  }

  private UUID getSenderIdFromToken() {
     return UUID.fromString(SecurityContextHolder.getContext().getAuthentication().getName());
  }
}
