package com.EntertainmentViet.backend.domain.businessLogic;

import com.EntertainmentViet.backend.domain.entities.Auditable;

import javax.persistence.PrePersist;
import java.time.OffsetDateTime;

public class AuditableListener {

  @PrePersist
  public void updateCreatedAtPrePersist(Auditable auditable) {
    auditable.setCreatedAt(OffsetDateTime.now());
  }
}
