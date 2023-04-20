package com.EntertainmentViet.backend.features.security.boundary;

import com.EntertainmentViet.backend.exception.KeycloakUnauthorizedException;
import com.EntertainmentViet.backend.exception.KeycloakUserConflictException;
import com.EntertainmentViet.backend.features.security.dto.CreatedKeycloakUserDto;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

public interface KeycloakBoundary {

  Optional<UUID> createUser(CreatedKeycloakUserDto userDto) throws KeycloakUnauthorizedException, KeycloakUserConflictException;

  boolean addUserToGroup(UUID uid, UUID groupsUid) throws KeycloakUnauthorizedException;

  Optional<String> getEmailToken(UUID userUid, EMAIL_ACTION action, String redirectUrl);

  void triggerEmailVerification(String token) throws IOException;

  enum EMAIL_ACTION {
    UPDATE_PASSWORD,
    VERIFY_EMAIL,
    ;
  }
}
