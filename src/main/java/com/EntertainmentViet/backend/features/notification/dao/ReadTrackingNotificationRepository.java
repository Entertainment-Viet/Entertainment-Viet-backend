package com.EntertainmentViet.backend.features.notification.dao;

import com.EntertainmentViet.backend.domain.entities.notification.ReadTrackingNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReadTrackingNotificationRepository extends JpaRepository<ReadTrackingNotification, Long> {
}
