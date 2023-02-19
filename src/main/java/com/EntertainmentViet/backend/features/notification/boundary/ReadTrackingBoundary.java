package com.EntertainmentViet.backend.features.notification.boundary;

import com.EntertainmentViet.backend.domain.entities.notification.ReadTrackingNotification;

import java.util.UUID;

public interface ReadTrackingBoundary {

  void sendNotification(UUID notifiedUserId, ReadTrackingNotification notification);
}
