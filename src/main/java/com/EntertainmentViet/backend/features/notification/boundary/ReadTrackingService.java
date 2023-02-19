package com.EntertainmentViet.backend.features.notification.boundary;

import com.EntertainmentViet.backend.config.constants.AppConstant;
import com.EntertainmentViet.backend.domain.entities.notification.ReadTrackingNotification;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReadTrackingService implements ReadTrackingBoundary {

  private final SimpMessagingTemplate messagingTemplate;

  @Override
  public void sendNotification(UUID notifiedUserId, ReadTrackingNotification notification) {
    messagingTemplate.convertAndSend(AppConstant.NOTIFICATION_LAST_READ_TOPIC, notification);
  }
}
