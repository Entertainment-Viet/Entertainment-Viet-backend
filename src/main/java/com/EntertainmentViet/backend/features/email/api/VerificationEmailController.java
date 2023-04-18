package com.EntertainmentViet.backend.features.email.api;

import com.EntertainmentViet.backend.features.email.boundary.EmailBoundary;
import com.EntertainmentViet.backend.features.security.boundary.KeycloakBoundary;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RestController
@RequiredArgsConstructor
@Async
@Validated
@RequestMapping(path = VerificationEmailController.REQUEST_MAPPING_PATH)
public class VerificationEmailController {

  public static final String REQUEST_MAPPING_PATH = "/email";

  public static final String VERIFICATION_PATH = "/verification";

  public static final String RESET_PASSWORD_PATH = "/pass-reset";

  private final EmailBoundary emailService;

  private final KeycloakBoundary keycloakService;

  @GetMapping(value = "/{uid}" + VERIFICATION_PATH)
  public CompletableFuture<ResponseEntity<Void>> sendVerificationEmail(@PathVariable("uid") UUID uid, @RequestParam(name = "redirectUrl") String redirectUrl) {
    var token = keycloakService.getEmailToken(uid, KeycloakBoundary.EMAIL_ACTION.VERIFY_EMAIL, redirectUrl).orElse("");
    emailService.sendVerificationEmail(uid, token);
    return CompletableFuture.completedFuture(ResponseEntity.ok().build());
  }
}
