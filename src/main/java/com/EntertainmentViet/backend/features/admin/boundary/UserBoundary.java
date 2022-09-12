package com.EntertainmentViet.backend.features.admin.boundary;

import com.EntertainmentViet.backend.exception.KeycloakUnauthorizedException;
import com.EntertainmentViet.backend.exception.KeycloakUserConflictException;
import com.EntertainmentViet.backend.features.organizer.dto.organizer.CreatedOrganizerDto;
import com.EntertainmentViet.backend.features.talent.dto.talent.CreatedTalentDto;

import java.util.Optional;
import java.util.UUID;

public interface UserBoundary {

  Optional<UUID> createOrganizer(CreatedOrganizerDto createdOrganizerDto) throws KeycloakUnauthorizedException, KeycloakUserConflictException;

  Optional<UUID> createTalent(CreatedTalentDto createdTalentDto) throws KeycloakUnauthorizedException, KeycloakUserConflictException;

  boolean verifyOrganizer(UUID uid) throws KeycloakUnauthorizedException;

  boolean verifyTalent(UUID uid) throws KeycloakUnauthorizedException;
}
