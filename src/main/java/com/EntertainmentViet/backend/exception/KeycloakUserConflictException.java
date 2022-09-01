package com.EntertainmentViet.backend.exception;

public class KeycloakUserConflictException extends Exception {
  public KeycloakUserConflictException(String username) {
    super(String.format("User with username '%s' is already existed", username));
  }
}
