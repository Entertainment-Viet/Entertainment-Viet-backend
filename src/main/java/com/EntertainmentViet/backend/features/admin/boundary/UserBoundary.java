package com.EntertainmentViet.backend.features.admin.boundary;

import com.EntertainmentViet.backend.exception.KeycloakUnauthorizedException;
import com.EntertainmentViet.backend.exception.KeycloakUserConflictException;
import com.EntertainmentViet.backend.features.organizer.dto.OrganizerDto;
import com.EntertainmentViet.backend.features.talent.dto.TalentDto;

import java.util.Optional;
import java.util.UUID;

public interface UserBoundary {

  Optional<UUID> createOrganizer(OrganizerDto organizerDto) throws KeycloakUnauthorizedException, KeycloakUserConflictException;

  Optional<UUID> createTalent(TalentDto talentDto) throws KeycloakUnauthorizedException, KeycloakUserConflictException;
}
