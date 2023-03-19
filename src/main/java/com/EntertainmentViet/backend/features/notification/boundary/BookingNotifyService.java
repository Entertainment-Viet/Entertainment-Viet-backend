package com.EntertainmentViet.backend.features.notification.boundary;

import com.EntertainmentViet.backend.config.constants.AppConstant;
import com.EntertainmentViet.backend.domain.entities.Account;
import com.EntertainmentViet.backend.domain.entities.booking.Booking;
import com.EntertainmentViet.backend.domain.entities.notification.BookingNotification;
import com.EntertainmentViet.backend.features.common.dao.AccountRepository;
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

  private final AccountRepository accountRepository;

  @Override
  public void sendCreateNotification(UUID receiverUid, String receiverName, Booking booking) {
    sendNotificationWithContent(receiverUid, receiverName, booking, ActionType.CREATE);
  }

  @Override
  public void sendUpdateNotification(UUID receiverUid, String receiverName, Booking booking) {
    sendNotificationWithContent(receiverUid, receiverName, booking, ActionType.UPDATE);
  }

  @Override
  public void sendAcceptNotification(UUID receiverUid, String receiverName, Booking booking) {
    sendNotificationWithContent(receiverUid, receiverName, booking, ActionType.ACCEPT);
  }

  @Override
  public void sendRejectNotification(UUID receiverUid, String receiverName, Booking booking) {
    sendNotificationWithContent(receiverUid, receiverName, booking, ActionType.REJECT);
  }

  @Override
  public void sendCancelNotification(UUID receiverUid, String receiverName, Booking booking) {
    sendNotificationWithContent(receiverUid, receiverName, booking, ActionType.CANCEL);
  }

  @Override
  public void sendFinishNotification(UUID receiverUid, String receiverName, Booking booking) {
    sendNotificationWithContent(receiverUid, receiverName, booking, ActionType.FINISH);
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

  private void sendNotificationWithContent(UUID receiverUid, String receiverName, Booking booking, ActionType actionType) {
    var senderUid = getSenderIdFromToken();
    var senderName = accountRepository.findByUid(senderUid).map(Account::getDisplayName).orElse("");
    var content = String.format("User '%s' just %s the booking '%s'",
        senderName, actionType.text, booking.getBookingCode());

    var notification = BookingNotification.builder()
        .senderUid(senderUid)
        .senderName(senderName)
        .recipientUid(receiverUid)
        .recipientName(receiverName)
        .bookingUid(booking.getUid())
        .content(content)
        .createdAt(OffsetDateTime.now())
        .isRead(false)
        .build();

    var savedNotification = bookingNotificationRepository.save(notification);
    sendNotification(receiverUid, savedNotification);
  }

  private UUID getSenderIdFromToken() {
     return UUID.fromString(SecurityContextHolder.getContext().getAuthentication().getName());
  }

  private enum ActionType {
    CREATE("create"),
    UPDATE("update"),
    ACCEPT("accept"),
    REJECT("reject"),
    CANCEL("cancel"),
    FINISH("finish"),
    ;

    public final String text;

    ActionType(String text) {
      this.text = text;
    }
  }
}
