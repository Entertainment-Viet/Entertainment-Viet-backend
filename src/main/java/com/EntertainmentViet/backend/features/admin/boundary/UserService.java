package com.EntertainmentViet.backend.features.admin.boundary;

import com.EntertainmentViet.backend.exception.KeycloakUnauthorizedException;
import com.EntertainmentViet.backend.exception.KeycloakUserConflictException;
import com.EntertainmentViet.backend.features.organizer.boundary.OrganizerBoundary;
import com.EntertainmentViet.backend.features.organizer.dto.OrganizerDto;
import com.EntertainmentViet.backend.features.security.boundary.KeycloakBoundary;
import com.EntertainmentViet.backend.features.security.dto.CreatedUserDto;
import com.EntertainmentViet.backend.features.talent.boundary.TalentBoundary;
import com.EntertainmentViet.backend.features.talent.dto.TalentDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService implements UserBoundary {

  private final OrganizerBoundary organizerService;

  private final TalentBoundary talentService;

  private final KeycloakBoundary keycloakService;

  @Override
  @Transactional(rollbackFor = {KeycloakUserConflictException.class, KeycloakUnauthorizedException.class})
  public Optional<UUID> createOrganizer(OrganizerDto organizerDto) throws KeycloakUnauthorizedException, KeycloakUserConflictException {
    var newUid = organizerService.create(organizerDto);

    if (newUid.isPresent()) {
      var createdOrganizerDto = CreatedUserDto.builder()
          .username(organizerDto.getDisplayName())
          .email(organizerDto.getEmail())
          .firstName(newUid.get().toString())
          .credentials(List.of(CreatedUserDto.CredentialDto.builder().value(organizerDto.getDisplayName()).build()))
          .realmRoles(List.of("GUEST_ORGANIZER"))
          .build();

      if (keycloakService.createUser(createdOrganizerDto)) {
        return newUid;
      }
    }
    return Optional.empty();
  }

  @Override
  @Transactional(rollbackFor = {KeycloakUserConflictException.class, KeycloakUnauthorizedException.class})
  public Optional<UUID> createTalent(TalentDto talentDto) throws KeycloakUnauthorizedException, KeycloakUserConflictException {
    var newUid = talentService.create(talentDto);

    if (newUid.isPresent()) {
      var createdTalentDto = CreatedUserDto.builder()
          .username(talentDto.getDisplayName())
          .email(talentDto.getEmail())
          .firstName(newUid.get().toString())
          .credentials(List.of(CreatedUserDto.CredentialDto.builder().value(talentDto.getDisplayName()).build()))
          .realmRoles(List.of("GUEST_TALENT"))
          .build();

      if (keycloakService.createUser(createdTalentDto)) {
        return newUid;
      }
    }
    return Optional.empty();  }



}
