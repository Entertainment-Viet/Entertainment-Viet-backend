package com.EntertainmentViet.backend.features.email.api;

import com.EntertainmentViet.backend.features.common.utils.TokenUtils;
import com.EntertainmentViet.backend.features.email.boundary.EmailBoundary;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
@RequiredArgsConstructor
@Async
@Validated
@RequestMapping(path = EmailProcessController.REQUEST_MAPPING_PATH)
public class EmailProcessController {

  public static final String REQUEST_MAPPING_PATH = "/email-process";

  public static final String VERIFICATION_PATH = "/verification";

  public static final String RESET_PASSWORD_PATH = "/pass-reset";

  private final EmailBoundary emailService;

  @GetMapping(VERIFICATION_PATH)
  public void sendVerificationEmail(@RequestParam(name = "key") String token, @RequestParam(name = "redirectUrl") String redirectUrl, HttpServletResponse response) throws IOException {
    emailService.processVerificationEmail(token);
    response.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
    response.setHeader("Location", TokenUtils.getRedirectUrl(token));
  }
}
