package com.EntertainmentViet.backend.features.admin.boundary;

import com.EntertainmentViet.backend.config.constants.KeycloakConstant;
import com.EntertainmentViet.backend.exception.KeycloakUnauthorizedException;
import com.EntertainmentViet.backend.exception.KeycloakUserConflictException;
import com.EntertainmentViet.backend.features.admin.dto.CreatedTalentDto;
import com.EntertainmentViet.backend.features.organizer.boundary.organizer.OrganizerBoundary;
import com.EntertainmentViet.backend.features.organizer.dto.organizer.CreatedOrganizerDto;
import com.EntertainmentViet.backend.features.organizer.dto.organizer.UpdateOrganizerDto;
import com.EntertainmentViet.backend.features.security.boundary.KeycloakBoundary;
import com.EntertainmentViet.backend.features.security.dto.CreatedKeycloakUserDto;
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
  public Optional<UUID> createOrganizer(CreatedOrganizerDto createdOrganizerDto) throws KeycloakUnauthorizedException, KeycloakUserConflictException {
    // TODO decrypt password after adding encryption feature

    var keycloakUserDto = CreatedKeycloakUserDto.builder()
        .username(createdOrganizerDto.getUsername())
        .email(createdOrganizerDto.getEmail())
        .firstName(createdOrganizerDto.getDisplayName())
        .credentials(List.of(CreatedKeycloakUserDto.CredentialDto.builder().value(createdOrganizerDto.getPassword()).build()))
        .groups(List.of("GUEST_ORGANIZER"))
        .build();

    var newUid = keycloakService.createUser(keycloakUserDto);

    if (newUid.isPresent()) {
      var organizerDto = UpdateOrganizerDto.builder()
          .displayName(createdOrganizerDto.getDisplayName())
          .email(createdOrganizerDto.getEmail())
          .phoneNumber(createdOrganizerDto.getPhoneNumber())
          .address(createdOrganizerDto.getAddress())
          .bio(createdOrganizerDto.getBio())
          .extensions(createdOrganizerDto.getExtensions())
          .build();

      return organizerService.create(organizerDto, newUid.get());
    }
    return Optional.empty();
  }

  @Override
  @Transactional(rollbackFor = {KeycloakUserConflictException.class, KeycloakUnauthorizedException.class})
  public Optional<UUID> createTalent(CreatedTalentDto createdTalentDto) throws KeycloakUnauthorizedException, KeycloakUserConflictException {
    // TODO decrypt password after adding encryption feature

    var keycloakUserDto = CreatedKeycloakUserDto.builder()
        .username(createdTalentDto.getUsername())
        .email(createdTalentDto.getEmail())
        .firstName(createdTalentDto.getDisplayName())
        .credentials(List.of(CreatedKeycloakUserDto.CredentialDto.builder().value(createdTalentDto.getPassword()).build()))
        .groups(List.of("GUEST_TALENT"))
        .build();

    var newUid = keycloakService.createUser(keycloakUserDto);

    if (newUid.isPresent()) {
      var talentDto = TalentDto.builder()
          .displayName(createdTalentDto.getDisplayName())
          .email(createdTalentDto.getEmail())
          .phoneNumber(createdTalentDto.getPhoneNumber())
          .address(createdTalentDto.getAddress())
          .bio(createdTalentDto.getBio())
          .build();

      return talentService.create(talentDto, newUid.get());
    }
    return Optional.empty();
  }

  @Override
  @Transactional(rollbackFor = {KeycloakUnauthorizedException.class})
  public boolean verifyOrganizer(UUID uid) throws KeycloakUnauthorizedException {
    return organizerService.verify(uid) && keycloakService.addUserToGroup(uid, KeycloakConstant.groupToId.get("VERIFIED_ORGANIZER"));
  }

  @Override
  @Transactional(rollbackFor = {KeycloakUnauthorizedException.class})
  public boolean verifyTalent(UUID uid) throws KeycloakUnauthorizedException {
    return talentService.verify(uid) && keycloakService.addUserToGroup(uid, KeycloakConstant.groupToId.get("VERIFIED_TALENT"));
  }
}
