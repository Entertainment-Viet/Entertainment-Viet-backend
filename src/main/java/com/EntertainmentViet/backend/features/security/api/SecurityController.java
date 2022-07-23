package com.EntertainmentViet.backend.features.security.api;

import com.EntertainmentViet.backend.features.organizer.api.OrganizerController;
import com.EntertainmentViet.backend.features.security.boundary.AuthenticationBoundary;
import com.EntertainmentViet.backend.features.security.dto.LoginInfoDto;
import com.EntertainmentViet.backend.features.security.dto.LoginTokenDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.KeycloakSecurityContext;
import org.keycloak.adapters.KeycloakDeployment;
import org.keycloak.adapters.RefreshableKeycloakSecurityContext;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.time.Duration;

@Controller
@RequestMapping(path = SecurityController.REQUEST_MAPPING_PATH)
@Slf4j
@RequiredArgsConstructor
public class SecurityController {

  public static final String REQUEST_MAPPING_PATH = "/auth";

  private static final Duration REQUEST_TIMEOUT = Duration.ofSeconds(3);

  private final AuthenticationBoundary authenticationService;


  /**
   * Makes Keycloak Login.
   *
   * @return {@link LoginTokenDto}
   */
  @PostMapping(path = "/login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public @ResponseBody LoginTokenDto login(@RequestBody LoginInfoDto loginInfo) {
    return authenticationService.login(REQUEST_TIMEOUT, loginInfo);
  }

  /**
   * Makes Keycloak Logout.
   *
   * @param request the request
   * @return redirect to logout page
   */
  @GetMapping(path = "/logout")
  public String logout(HttpServletRequest request) {
    authenticationService.logout(request);
    return String.format("redirect:%s/1", OrganizerController.REQUEST_MAPPING_PATH);
  }

}
