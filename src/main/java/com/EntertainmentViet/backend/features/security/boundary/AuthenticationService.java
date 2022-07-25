package com.EntertainmentViet.backend.features.security.boundary;

import com.EntertainmentViet.backend.config.properties.KeycloakProperties;
import com.EntertainmentViet.backend.config.properties.SecretProperties;
import com.EntertainmentViet.backend.features.security.dto.*;
import com.EntertainmentViet.backend.utils.JwtUtils;
import com.nimbusds.jwt.JWTClaimsSet;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationService implements AuthenticationBoundary {

  private final WebClient keycloakTokenApiClient;

  private final KeycloakTokenMapper keycloakTokenMapper;

  private final KeycloakProperties keycloakProperties;

  private final SecretProperties secretProperties;

  @Override
  public Optional<LoginResponseDto> login(Duration timeout, LoginRequestDto request) {
    var loginResponse = keycloakTokenApiClient.post()
        .uri("/protocol/openid-connect/token")
        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
        .body(BodyInserters.fromFormData("grant_type", "password")
            .with("username", request.getUsername())
            .with("password", request.getPassword())
            .with("client_id", keycloakProperties.getResource())
            .with("client_secret", secretProperties.getClientSecret())
            .with("scope", "openid")
        )
        .retrieve()
        .onStatus(HttpStatus.UNAUTHORIZED::equals,
            response -> response.bodyToMono(String.class).map(Exception::new))
        .bodyToMono(KeycloakTokenDto.class)
        .onErrorMap(e -> {
          log.warn("Can not login to keycloak for user: " + request.getUsername());
          return e;
        })
        .onErrorReturn(new KeycloakTokenDto())
        .block(timeout);

    if (loginResponse.getAccessToken() == null) {
      return Optional.empty();
    }
    return Optional.of(keycloakTokenMapper.toLoginResponseDto(loginResponse));
  }

  @Override
  public boolean logout(Duration timeout, LogoutRequestDto request) {
    String requestUser = JwtUtils.getPayloadFromToken(request.getIdToken())
                                 .map(jwtClaimsSet -> jwtClaimsSet.getClaims().get(JwtUtils.PREFERRED_USERNAME).toString())
                                 .orElse(null);

    if (requestUser == null) {
      return false; // Indicate error
    }

    keycloakTokenApiClient.post()
        .uri("/protocol/openid-connect/logout")
        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
        .body(BodyInserters.fromFormData("refresh_token", request.getRefreshToken())
            .with("client_id", keycloakProperties.getResource())
            .with("client_secret", secretProperties.getClientSecret())
        )
        .retrieve()
        .bodyToMono(Void.class)
        .onErrorMap(e -> {
          log.error("Can not logout user: " + requestUser);
          return e;
        })
        .block(timeout);

    return true;
  }

  @Override
  public Optional<LoginResponseDto> refreshToken(Duration timeout, RefreshRequestDto request) {
    String requestUser = JwtUtils.getPayloadFromToken(request.getIdToken())
        .map(jwtClaimsSet -> jwtClaimsSet.getClaims().get(JwtUtils.PREFERRED_USERNAME).toString())
        .orElse(null);

    if (requestUser == null) {
      return Optional.empty();
    }

    var loginResponse = keycloakTokenApiClient.post()
        .uri("/protocol/openid-connect/token")
        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
        .body(BodyInserters.fromFormData("grant_type", "refresh_token")
            .with("refresh_token", request.getRefreshToken())
            .with("client_id", keycloakProperties.getResource())
            .with("client_secret", secretProperties.getClientSecret())
            .with("scope", "openid")
        )
        .retrieve()
        .onStatus(HttpStatus.BAD_REQUEST::equals,
            response -> response.bodyToMono(String.class).map(Exception::new))
        .bodyToMono(KeycloakTokenDto.class)
        .onErrorMap(e -> {
          log.warn("Can not get access token for user: " + requestUser);
          return e;
        })
        .onErrorReturn(new KeycloakTokenDto())
        .block(timeout);

    if (loginResponse.getAccessToken() == null) {
      return Optional.empty();
    }
    return Optional.of(keycloakTokenMapper.toLoginResponseDto(loginResponse));
  }

}
