package com.EntertainmentViet.backend.features.email.api;

import com.EntertainmentViet.backend.features.common.utils.TokenUtils;
import com.EntertainmentViet.backend.features.email.boundary.EmailBoundary;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RestController
@RequiredArgsConstructor
@Async
@Validated
@RequestMapping(path = EmailController.REQUEST_MAPPING_PATH)
@Slf4j
public class EmailController {

  public static final String REQUEST_MAPPING_PATH = "/email/{uid}";

  public static final String VERIFICATION_PATH = "/verification";

  public static final String RESET_PASSWORD_PATH = "/pass-reset";

  private final EmailBoundary emailService;

  private final ServletContext servletContext;

  @GetMapping(VERIFICATION_PATH)
  public CompletableFuture<ResponseEntity<Void>> sendVerificationEmail(JwtAuthenticationToken token, @PathVariable("uid") UUID uid, @RequestParam(name = "redirectUrl") String redirectUrl, HttpServletRequest request) {
    if (!uid.equals(TokenUtils.getUid(token)) && !TokenUtils.isTokenContainPermissions(token, "ROOT")) {
      log.warn(String.format("The token don't have enough access right to trigger sending email for user with uid '%s'",
          uid));
      return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }

    String baseUrl = ServletUriComponentsBuilder.fromRequestUri(request)
        .replacePath(servletContext.getContextPath() + EmailProcessController.REQUEST_MAPPING_PATH + EmailProcessController.VERIFICATION_PATH)
        .build()
        .toUriString();
    emailService.sendVerificationEmail(uid, baseUrl, redirectUrl);
    return CompletableFuture.completedFuture(ResponseEntity.ok().build());
  }

  @GetMapping(RESET_PASSWORD_PATH)
  public CompletableFuture<ResponseEntity<Void>> sendResetPassEmail(JwtAuthenticationToken token, @PathVariable("uid") UUID uid, @RequestParam(name = "redirectUrl") String redirectUrl, HttpServletRequest request) {
    if (!uid.equals(TokenUtils.getUid(token)) && !TokenUtils.isTokenContainPermissions(token, "ROOT")) {
      log.warn(String.format("The token don't have enough access right to trigger sending email for user with uid '%s'",
          uid));
      return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }

    String baseUrl = ServletUriComponentsBuilder.fromRequestUri(request)
        .replacePath(servletContext.getContextPath() + EmailProcessController.REQUEST_MAPPING_PATH + EmailProcessController.RESET_PASSWORD_PATH)
        .queryParam("redirectUrl", redirectUrl)
        .build()
        .toUriString();
    emailService.sendResetPasswordEmail(uid, baseUrl, redirectUrl);
    return CompletableFuture.completedFuture(ResponseEntity.ok().build());
  }
}
