package com.EntertainmentViet.backend.features.email.boundary;

import com.EntertainmentViet.backend.features.email.dto.ResetEmailDto;
import com.EntertainmentViet.backend.features.security.dto.CredentialDto;

import java.util.UUID;

public interface EmailBoundary {

  void sendVerificationEmail(UUID uid, String baseUrl, String redirectUrl);

  void sendResetPasswordEmail(ResetEmailDto resetEmailDto, String baseUrl, String redirectUrl);

  void processVerificationEmail(String token);

  void processResetPasswordEmail(String token, CredentialDto credentialDto);
}
