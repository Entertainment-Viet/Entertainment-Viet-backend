package com.EntertainmentViet.backend.features.notification.dao;

import com.EntertainmentViet.backend.domain.entities.notification.BookingNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingNotificationRepository extends JpaRepository<BookingNotification, Long> {
}
