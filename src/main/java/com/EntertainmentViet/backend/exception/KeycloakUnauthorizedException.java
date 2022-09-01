package com.EntertainmentViet.backend.exception;

public class KeycloakUnauthorizedException extends Exception {
  public KeycloakUnauthorizedException() {
    super("Can not authorized to keycloak server. " +
        "Please check authentication.keycloak.adminUsername and authentication.keycloak.adminPassword properties");
  }
}
