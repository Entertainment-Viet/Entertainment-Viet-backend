package com.EntertainmentViet.backend.domain.entities;

import java.time.OffsetDateTime;

public interface Auditable {
  void setCreatedAt(OffsetDateTime instant);
  OffsetDateTime getCreatedAt();
}
