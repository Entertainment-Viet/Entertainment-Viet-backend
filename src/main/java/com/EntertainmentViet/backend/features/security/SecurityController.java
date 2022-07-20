package com.EntertainmentViet.backend.features.security;

import com.EntertainmentViet.backend.features.organizer.api.OrganizerController;
import org.keycloak.KeycloakSecurityContext;
import org.keycloak.adapters.KeycloakDeployment;
import org.keycloak.adapters.RefreshableKeycloakSecurityContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping(path = SecurityController.REQUEST_MAPPING_PATH)
public class SecurityController {

  public static final String REQUEST_MAPPING_PATH = "/sso";

  /**
   * Makes SSO Logout.
   * This endpoint has to be private. Otherwise there will be no token to send logout to KeyCloak.
   *
   * @param request the request
   * @return redirect to logout page
   * @throws ServletException if tomcat session logout throws exception
   */
  @GetMapping(path = "/logout")
  public String logout(HttpServletRequest request) throws ServletException {
    keycloakSessionLogout(request);
    servletSessionLogout(request);

    String redirectedUrl = String.format("redirect:%s/1", OrganizerController.REQUEST_MAPPING_PATH);
    return redirectedUrl;
  }


  private void servletSessionLogout(HttpServletRequest request) throws ServletException {
    request.logout();
  }

  private void keycloakSessionLogout(HttpServletRequest request){
    RefreshableKeycloakSecurityContext context = getKeycloakSecurityContext(request);
    KeycloakDeployment keycloakDeployment = context.getDeployment();
    context.logout(keycloakDeployment);
  }

  private RefreshableKeycloakSecurityContext getKeycloakSecurityContext(HttpServletRequest request){
    return (RefreshableKeycloakSecurityContext) request.getAttribute(KeycloakSecurityContext.class.getName());
  }
}
