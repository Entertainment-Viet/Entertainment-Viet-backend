package com.EntertainmentViet.backend.features.security.boundary;

import com.EntertainmentViet.backend.exception.KeycloakUnauthorizedException;
import com.EntertainmentViet.backend.exception.KeycloakUserConflictException;
import com.EntertainmentViet.backend.features.security.dto.CreatedUserDto;

public interface KeycloakBoundary {

  boolean createUser(CreatedUserDto userDto) throws KeycloakUnauthorizedException, KeycloakUserConflictException;
}
