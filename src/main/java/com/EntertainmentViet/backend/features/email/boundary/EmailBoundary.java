package com.EntertainmentViet.backend.features.email.boundary;

import java.util.UUID;

public interface EmailBoundary {

  void sendVerificationEmail(UUID uid, String token);
}
