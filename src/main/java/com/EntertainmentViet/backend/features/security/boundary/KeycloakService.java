package com.EntertainmentViet.backend.features.security.boundary;

import com.EntertainmentViet.backend.config.properties.AuthenticationProperties;
import com.EntertainmentViet.backend.exception.KeycloakUnauthorizedException;
import com.EntertainmentViet.backend.exception.KeycloakUserConflictException;
import com.EntertainmentViet.backend.features.security.dto.CreatedUserDto;
import com.EntertainmentViet.backend.features.security.dto.LoginInfoDto;
import com.EntertainmentViet.backend.features.security.dto.TokenDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class KeycloakService implements KeycloakBoundary {

  private final AuthenticationProperties authenticationProperties;

  private final RestTemplate keycloakRestTemplate;

  @Override
  public boolean createUser(CreatedUserDto userDto) throws KeycloakUnauthorizedException, KeycloakUserConflictException {
    String createdUserUrl = String.format("/admin/realms/%s/users",
        authenticationProperties.getKeycloak().getRealm());
    String token = getAdminToken();

    HttpHeaders headers = new HttpHeaders();
    headers.setBearerAuth(token);
    headers.setContentType(MediaType.APPLICATION_JSON);
    HttpEntity<CreatedUserDto> request = new HttpEntity<>(userDto, headers);

    try {
      keycloakRestTemplate.postForObject(createdUserUrl, request, Void.class);
      return true;
    } catch (HttpStatusCodeException ex) {
      if (ex.getStatusCode().equals(HttpStatus.UNAUTHORIZED)) {
        throw new KeycloakUnauthorizedException();
      } else if (ex.getStatusCode().equals(HttpStatus.CONFLICT)) {
        throw new KeycloakUserConflictException(userDto.getUsername());
      } else {
        log.error("Can not request to keycloak server ", ex);
      }
    }

    return false;
  }

  public Optional<TokenDto> login(LoginInfoDto loginInfoDto) {
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

  private String getAdminToken() {
    var adminCred = LoginInfoDto.builder()
        .username(authenticationProperties.getKeycloak().getAdminUsername())
        .password(authenticationProperties.getKeycloak().getAdminPassword())
        .build();

    return login(adminCred).map(TokenDto::getAccessToken).orElse(null);
  }
}
