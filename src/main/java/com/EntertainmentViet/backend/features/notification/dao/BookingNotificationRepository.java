package com.EntertainmentViet.backend.features.notification.dao;

import com.EntertainmentViet.backend.domain.entities.notification.BookingNotification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Repository
@Transactional
public interface BookingNotificationRepository extends PagingAndSortingRepository<BookingNotification, Long> {

  Page<BookingNotification> findByOrderByCreatedAtDesc(Pageable pageable);

  @Modifying
  @Query("UPDATE BookingNotification SET isRead = :read WHERE recipientUid = :uid")
  void updateIsRead(@Param(value = "read") boolean read, @Param(value = "uid") UUID uid);
}
