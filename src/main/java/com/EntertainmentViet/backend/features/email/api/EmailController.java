package com.EntertainmentViet.backend.features.email.api;

import com.EntertainmentViet.backend.config.properties.AuthenticationProperties;
import com.EntertainmentViet.backend.features.email.boundary.EmailBoundary;
import com.EntertainmentViet.backend.features.email.dto.ResetPassEmailDto;
import com.EntertainmentViet.backend.features.email.dto.VerifyEmailDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.concurrent.CompletableFuture;

@RestController
@RequiredArgsConstructor
@Async
@Validated
@RequestMapping(path = EmailController.REQUEST_MAPPING_PATH)
@Slf4j
public class EmailController {

  public static final String REQUEST_MAPPING_PATH = "/email";

  public static final String VERIFICATION_PATH = "/verification";

  public static final String RESET_PASSWORD_PATH = "/pass-reset";

  private final EmailBoundary emailService;

  private final ServletContext servletContext;

  private final AuthenticationProperties authenticationProperties;

  @PostMapping(VERIFICATION_PATH)
  public CompletableFuture<ResponseEntity<Void>> sendVerificationEmail(@RequestBody @Valid VerifyEmailDto verifyEmailDto, @RequestParam(name = "redirectUrl") String redirectUrl, HttpServletRequest request) {

    String baseUrl = ServletUriComponentsBuilder.fromRequestUri(request)
        .replacePath(servletContext.getContextPath() + EmailProcessController.REQUEST_MAPPING_PATH + EmailProcessController.VERIFICATION_PATH)
        .build()
        .toUriString();
    emailService.sendVerificationEmail(verifyEmailDto.getUid(), baseUrl, redirectUrl);
    return CompletableFuture.completedFuture(ResponseEntity.ok().build());
  }

  @PostMapping(RESET_PASSWORD_PATH)
  public CompletableFuture<ResponseEntity<Void>> sendResetPassEmail(@RequestParam(name = "redirectUrl") String redirectUrl,
                                                                    @RequestBody @Valid ResetPassEmailDto resetPassEmailDto) {
    String baseUrl = UriComponentsBuilder.fromUriString(authenticationProperties.getKeycloak().getResetPasswordUrl())
        .build()
        .toUriString();
    emailService.sendResetPasswordEmail(resetPassEmailDto, baseUrl, redirectUrl);
    return CompletableFuture.completedFuture(ResponseEntity.ok().build());
  }
}
