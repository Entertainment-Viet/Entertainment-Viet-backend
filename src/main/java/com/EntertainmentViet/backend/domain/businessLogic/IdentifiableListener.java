package com.EntertainmentViet.backend.domain.businessLogic;

import com.EntertainmentViet.backend.domain.entities.Identifiable;

import javax.persistence.PrePersist;
import java.util.UUID;

public class IdentifiableListener {

  @PrePersist
  public void updateUidPrePersist(Identifiable identifiable) {
    if (identifiable.getUid() == null) {
      identifiable.setUid(UUID.randomUUID());
    }
  }
}
