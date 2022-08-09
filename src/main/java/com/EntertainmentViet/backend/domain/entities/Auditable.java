package com.EntertainmentViet.backend.domain.entities;

import java.time.Instant;

public interface Auditable {
  void setCreatedAt(Instant instant);
  Instant getCreatedAt();
}
