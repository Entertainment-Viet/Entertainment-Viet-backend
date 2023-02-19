package com.EntertainmentViet.backend.domain.entities.notification;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReadTrackingNotification {
  @Id
  @GeneratedValue
  private Long id;

  private UUID userId;
  private OffsetDateTime readUntil;
}
