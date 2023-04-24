package com.EntertainmentViet.backend.features.email.api;

import com.EntertainmentViet.backend.features.common.utils.TokenUtils;
import com.EntertainmentViet.backend.features.email.boundary.EmailBoundary;
import com.EntertainmentViet.backend.features.security.dto.CredentialDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;

@Controller
@RequiredArgsConstructor
@Validated
@RequestMapping(path = EmailProcessController.REQUEST_MAPPING_PATH)
public class EmailProcessController {

  public static final String REQUEST_MAPPING_PATH = "/email-process";

  public static final String VERIFICATION_PATH = "/verification";

  public static final String RESET_PASSWORD_PATH = "/pass-reset";

  private final EmailBoundary emailService;

  @GetMapping(VERIFICATION_PATH)
  public void processVerificationEmail(@RequestParam(name = "key") String keyToken, HttpServletResponse response) throws IOException {
    emailService.processVerificationEmail(keyToken);
    response.setStatus(HttpServletResponse.SC_SEE_OTHER);
    response.setHeader("Location", TokenUtils.getRedirectUrl(keyToken));
  }

  @PostMapping(RESET_PASSWORD_PATH)
  public void processResetPasswordEmail(@RequestParam(name = "key") String keyToken, HttpServletResponse response,
                                        @RequestBody @Valid CredentialDto credentialDto) throws IOException {
    emailService.processResetPasswordEmail(keyToken, credentialDto);
    response.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
    response.setHeader("Location", TokenUtils.getRedirectUrl(keyToken));
  }
}
