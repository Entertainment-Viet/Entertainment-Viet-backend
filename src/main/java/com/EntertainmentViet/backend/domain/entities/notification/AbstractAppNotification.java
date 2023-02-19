package com.EntertainmentViet.backend.domain.entities.notification;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public abstract class AbstractAppNotification {
  @Id
  @GeneratedValue
  private Long id;

  private UUID senderUid;
  private UUID recipientUid;
  private String content;
  private OffsetDateTime createdAt;
  private Boolean isRead;
}
