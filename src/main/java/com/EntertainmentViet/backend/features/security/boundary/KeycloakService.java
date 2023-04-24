package com.EntertainmentViet.backend.features.security.boundary;

import com.EntertainmentViet.backend.config.constants.KeycloakConstant;
import com.EntertainmentViet.backend.config.properties.AuthenticationProperties;
import com.EntertainmentViet.backend.exception.KeycloakUnauthorizedException;
import com.EntertainmentViet.backend.exception.KeycloakUserConflictException;
import com.EntertainmentViet.backend.features.common.utils.TokenUtils;
import com.EntertainmentViet.backend.features.email.EMAIL_ACTION;
import com.EntertainmentViet.backend.features.security.dto.*;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class KeycloakService implements KeycloakBoundary {

  private final AuthenticationProperties authenticationProperties;

  private final RestTemplate keycloakRestTemplate;

  private final String CUSTOM_ACTION_TOKEN_PATH = "/custom-action-tokens/action-tokens";

  private final String ACTION_TOKEN_PATH = "/login-actions/action-token";

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
      keycloakRestTemplate.put(url, request);
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

  @Override
  public void triggerEmailVerification(String token) throws IOException {
    String baseUrl = String.format("/realms/%s%s",
        authenticationProperties.getKeycloak().getRealm(), ACTION_TOKEN_PATH);

    // Send first request to acquire auth session
    String emailVerificationInitialUrl = UriComponentsBuilder.fromUri(keycloakRestTemplate.getUriTemplateHandler().expand(baseUrl))
        .queryParam("key", token)
        .build()
        .toUriString();

    Connection.Response res = Jsoup.connect(emailVerificationInitialUrl)
        .method(Connection.Method.GET)
        .ignoreHttpErrors(true)
        .execute();

    if (res.statusCode() != 200) {
      log.error("Provide token is invalid when processing email verification");
      return;
    }
    Document doc = res.parse();

    Elements results = doc.select(String.format(":containsOwn(%s)", EMAIL_ACTION.VERIFY_EMAIL.text));
    if (results.isEmpty()) {
      log.error("Invalid response from keycloak server when requesting email verification");
      return;
    }

    Element result = doc.select("a[href*=auth/realms/ve-sso/login-actions/action-token]").first();
    String authId = res.cookie("AUTH_SESSION_ID_LEGACY");
    String link = result.attr("href");
    UriComponents uriComponents = UriComponentsBuilder.fromUriString(link).build();
    var queryMap = uriComponents.getQueryParams().toSingleValueMap();

    // Send second request to actually do the email verification
    MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
    params.add("key", queryMap.get("key"));
    params.add("client_id", queryMap.get("client_id"));
    params.add("tab_id", queryMap.get("tab_id"));
    params.add("AUTH_SESSION_ID", authId);

    var emailVerifyProcessUrl = UriComponentsBuilder.fromUri(keycloakRestTemplate.getUriTemplateHandler().expand(baseUrl))
        .queryParams(params)
        .build()
        .toUriString();

    Jsoup.connect(emailVerifyProcessUrl)
        .cookies(res.cookies())
        .method(Connection.Method.GET)
        .ignoreHttpErrors(true)
        .execute();
  }

  @Override
  public void triggerPasswordReset(String token, CredentialDto credentialDto) throws IOException, KeycloakUnauthorizedException {
    String baseUrl = String.format("/realms/%s%s",
        authenticationProperties.getKeycloak().getRealm(), ACTION_TOKEN_PATH);

    // Send first request to acquire auth session
    String emailVerificationInitialUrl = UriComponentsBuilder.fromUri(keycloakRestTemplate.getUriTemplateHandler().expand(baseUrl))
        .queryParam("key", token)
        .build()
        .toUriString();

    Connection.Response res = Jsoup.connect(emailVerificationInitialUrl)
        .method(Connection.Method.GET)
        .ignoreHttpErrors(true)
        .execute();

    if (res.statusCode() != 200) {
      log.error("Provide token is invalid when processing reset password");
      return;
    }
    Document doc = res.parse();

    Elements results = doc.select(String.format(":containsOwn(%s)", EMAIL_ACTION.UPDATE_PASSWORD.text));
    if (results.isEmpty()) {
      log.error("Invalid response from keycloak server when requesting password reset");
      return;
    }

    var userUid = TokenUtils.getUid(token);
    if (userUid == null) {
      log.error("Can not find user uid in key token");
      return;
    }

    String resetPassUrl = String.format("/admin/realms/%s/users/%s/reset-password",
        authenticationProperties.getKeycloak().getRealm(), userUid);
    String adminToken = getAdminToken();

    HttpHeaders headers = new HttpHeaders();
    headers.setBearerAuth(adminToken);
    headers.setContentType(MediaType.APPLICATION_JSON);
    HttpEntity<CredentialDto> request = new HttpEntity<>(credentialDto, headers);

    try {
      keycloakRestTemplate.exchange(resetPassUrl, HttpMethod.PUT, request, Void.class);
    } catch (HttpStatusCodeException ex) {
      if (ex.getStatusCode().equals(HttpStatus.UNAUTHORIZED)) {
        throw new KeycloakUnauthorizedException();
      } else {
        log.error("Can not request to keycloak server ", ex);
      }
    }
  }

  private boolean checkResponseIsValid(Connection.Response res, EMAIL_ACTION email_action) {
    if (res.statusCode() != 200) {
      return false;
    }

    Document doc = null;
    try {
      doc = res.parse();
    } catch (IOException e) {
      return false;
    }
    Elements results = doc.select(String.format(":containsOwn(%s)", email_action.text));
    return !results.isEmpty();
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

    MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
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

  public Optional<String> getEmailToken(UUID userUid, EMAIL_ACTION action, String redirectUrl) {
    String emailTokenUrl =  String.format("/realms/%s%s",
        authenticationProperties.getKeycloak().getRealm(), CUSTOM_ACTION_TOKEN_PATH);

    MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
    params.add("userId", userUid.toString());
    params.add("redirectUri", redirectUrl);
    params.add("clientId", authenticationProperties.getKeycloak().getResource());
    params.add("action", action.name());

    URI url = UriComponentsBuilder.fromUri(keycloakRestTemplate.getUriTemplateHandler().expand(emailTokenUrl))
        .queryParams(params)
        .build()
        .toUri();

    HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(new HttpHeaders());

    try {
      return Optional.ofNullable(keycloakRestTemplate.postForObject(url, request, String.class));
    } catch (HttpStatusCodeException ex) {
      if (ex.getStatusCode().equals(HttpStatus.UNAUTHORIZED)) {
        log.error("Can not authorized to keycloak server. " +
            "Please check authentication.keycloak.adminUsername and authentication.keycloak.adminPassword properties");
      } else {
        log.error("Can not get email token from keycloak server ", ex);
      }
    }

    return Optional.empty();
  }
}
