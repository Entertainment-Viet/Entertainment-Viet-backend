package com.EntertainmentViet.backend.features.security.boundary;

import com.EntertainmentViet.backend.config.constants.KeycloakConstant;
import com.EntertainmentViet.backend.config.properties.AuthenticationProperties;
import com.EntertainmentViet.backend.exception.KeycloakUnauthorizedException;
import com.EntertainmentViet.backend.exception.KeycloakUserConflictException;
import com.EntertainmentViet.backend.features.security.dto.CreatedKeycloakUserDto;
import com.EntertainmentViet.backend.features.security.dto.GroupInfoDto;
import com.EntertainmentViet.backend.features.security.dto.LoginInfoDto;
import com.EntertainmentViet.backend.features.security.dto.TokenDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class KeycloakService implements KeycloakBoundary {

  private final AuthenticationProperties authenticationProperties;

  private final RestTemplate keycloakRestTemplate;

  public KeycloakService(AuthenticationProperties authenticationProperties, RestTemplate keycloakRestTemplate) {
    this.authenticationProperties = authenticationProperties;
    this.keycloakRestTemplate = keycloakRestTemplate;

    setupGroupsInfoConstant();
  }

  @Override
  public Optional<UUID> createUser(CreatedKeycloakUserDto userDto) throws KeycloakUnauthorizedException, KeycloakUserConflictException {
    String createdUserUrl = String.format("/admin/realms/%s/users",
        authenticationProperties.getKeycloak().getRealm());
    String token = getAdminToken();

    HttpHeaders headers = new HttpHeaders();
    headers.setBearerAuth(token);
    headers.setContentType(MediaType.APPLICATION_JSON);
    HttpEntity<CreatedKeycloakUserDto> request = new HttpEntity<>(userDto, headers);

    try {
      HttpEntity<Void> response = keycloakRestTemplate.exchange(createdUserUrl, HttpMethod.POST, request, Void.class);
      String path = response.getHeaders().getLocation().getPath();
      String uuidStr = path.substring(path.lastIndexOf('/') +1);
      return Optional.of(UUID.fromString(uuidStr));
    } catch (HttpStatusCodeException ex) {
      if (ex.getStatusCode().equals(HttpStatus.UNAUTHORIZED)) {
        throw new KeycloakUnauthorizedException();
      } else if (ex.getStatusCode().equals(HttpStatus.CONFLICT)) {
        throw new KeycloakUserConflictException(userDto.getUsername());
      } else {
        log.error("Can not request to keycloak server ", ex);
      }
    }

    return Optional.empty();
  }

  @Override
  public boolean addUserToGroup(UUID uid, UUID groupsUid) throws KeycloakUnauthorizedException{
    String url = String.format("/admin/realms/%s/users/%s/groups/%s",
        authenticationProperties.getKeycloak().getRealm(), uid.toString(), groupsUid.toString());
    String token = getAdminToken();

    HttpHeaders headers = new HttpHeaders();
    headers.setBearerAuth(token);
    HttpEntity<CreatedKeycloakUserDto> request = new HttpEntity<>(null, headers);

    try {
//      keycloakRestTemplate.put(url, request);
      return true;
    } catch (HttpStatusCodeException ex) {
      if (ex.getStatusCode().equals(HttpStatus.UNAUTHORIZED)) {
        throw new KeycloakUnauthorizedException();
      } else {
        log.error("Can not request to keycloak server ", ex);
      }
    }
    return false;
  }

  private void setupGroupsInfoConstant() {
    String groupsUrl = String.format("/admin/realms/%s/groups",
        authenticationProperties.getKeycloak().getRealm());
    String token = getAdminToken();

    HttpHeaders headers = new HttpHeaders();
    headers.setBearerAuth(token);
    HttpEntity<CreatedKeycloakUserDto> request = new HttpEntity<>(null, headers);

    try {
      List<GroupInfoDto> groupsResponse = keycloakRestTemplate.exchange(
          groupsUrl, HttpMethod.GET, request, new ParameterizedTypeReference<List<GroupInfoDto>>(){}).getBody();

      for (GroupInfoDto group : groupsResponse) {
        KeycloakConstant.groupToId.put(group.getName(), UUID.fromString(group.getId()));
      }
    } catch (HttpStatusCodeException ex) {
      if (ex.getStatusCode().equals(HttpStatus.UNAUTHORIZED)) {
        log.error("Can not setup KeycloakConstant due to unauthorized", new KeycloakUnauthorizedException());
      } else {
        log.error("Can not request to keycloak server ", ex);
      }
    }
  }

  private String getAdminToken() {
    var adminCred = LoginInfoDto.builder()
        .username(authenticationProperties.getKeycloak().getAdminUsername())
        .password(authenticationProperties.getKeycloak().getAdminPassword())
        .build();

    return login(adminCred).map(TokenDto::getAccessToken).orElse(null);
  }

  private Optional<TokenDto> login(LoginInfoDto loginInfoDto) {
    String loginUrl =  String.format("/realms/%s/protocol/openid-connect/token",
        authenticationProperties.getKeycloak().getRealm());

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

    MultiValueMap<String, String> map= new LinkedMultiValueMap<>();
    map.add("grant_type", "password");
    map.add("username", loginInfoDto.getUsername());
    map.add("password", loginInfoDto.getPassword());
    map.add("client_id", authenticationProperties.getKeycloak().getResource());
    map.add("scope", "openid");
    HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

    try {
      return Optional.ofNullable(keycloakRestTemplate.postForObject(loginUrl, request, TokenDto.class));
    } catch (HttpStatusCodeException ex) {
      if (ex.getStatusCode().equals(HttpStatus.UNAUTHORIZED)) {
        log.error("Can not authorized to keycloak server. " +
            "Please check authentication.keycloak.adminUsername and authentication.keycloak.adminPassword properties");
      } else {
        log.error("Can not request to keycloak server ", ex);
      }
    }

    return Optional.empty();
  }
}
