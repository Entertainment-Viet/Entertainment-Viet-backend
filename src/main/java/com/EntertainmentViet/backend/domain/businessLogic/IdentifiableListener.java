package com.EntertainmentViet.backend.domain.businessLogic;

import com.EntertainmentViet.backend.domain.entities.Identifiable;

import javax.persistence.PrePersist;
import java.util.UUID;

public class IdentifiableListener {

  @PrePersist
  public void updateUidPrePersist(Identifiable identifiable) {
    identifiable.setUid(UUID.randomUUID());
  }
}
