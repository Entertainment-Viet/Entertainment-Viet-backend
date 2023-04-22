package com.EntertainmentViet.backend.features.email.boundary;

import com.EntertainmentViet.backend.config.properties.AuthenticationProperties;
import com.EntertainmentViet.backend.config.properties.EmailProperties;
import com.EntertainmentViet.backend.domain.entities.Account;
import com.EntertainmentViet.backend.domain.entities.organizer.Organizer;
import com.EntertainmentViet.backend.domain.entities.talent.Talent;
import com.EntertainmentViet.backend.exception.KeycloakUnauthorizedException;
import com.EntertainmentViet.backend.features.common.dao.AccountRepository;
import com.EntertainmentViet.backend.features.email.EMAIL_ACTION;
import com.EntertainmentViet.backend.features.email.dto.EmailDetail;
import com.EntertainmentViet.backend.features.security.boundary.KeycloakBoundary;
import com.EntertainmentViet.backend.features.security.dto.CredentialDto;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import org.springframework.web.util.UriComponentsBuilder;

import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService implements EmailBoundary {

  private final JavaMailSender mailSender;

  private final EmailProperties emailProperties;

  private final FreeMarkerConfigurer freemarkerConfigurer;

  private final AuthenticationProperties authenticationProperties;

  private final AccountRepository accountRepository;

  private final KeycloakBoundary keycloakService;

  private final String VERIFICATION_TEMPLATE_EMAIL_PATH = "emails/verificationEmail.ftlh";

  private final String RESET_PASSWORD_TEMPLATE_EMAIL_PATH = "emails/resetPasswordEmail.ftlh";

  private final String DEFAULT_SENDER = "noreply@heartofshow.com";

  @Override
  @Async
  public void sendVerificationEmail(UUID uid, String baseUrl, String redirectUrl) {
    var recipientAccount = accountRepository.findByUid(uid);
    if (recipientAccount.isEmpty()) {
      log.error("Can not find email recipient with uuid: " + uid);
      return;
    }

    EmailDetail emailDetail = EmailDetail.builder()
        .sender(DEFAULT_SENDER)
        .recipient(getRecipientEmail(recipientAccount.get()))
        .subject("Email Verification")
        .contentTemplatePath(VERIFICATION_TEMPLATE_EMAIL_PATH)
        .build();

    var token = keycloakService.getEmailToken(uid, EMAIL_ACTION.VERIFY_EMAIL, redirectUrl).orElse("");
    String url = UriComponentsBuilder
        .fromHttpUrl(baseUrl)
        .queryParam("key", token)
        .queryParam("redirectUrl", redirectUrl)
        .toUriString();
    emailDetail.setVerificationUrl(url);
    emailDetail.setRecipientName(recipientAccount.get().getDisplayName());

    try {
      sendEmail(emailDetail);
    } catch (MessagingException e) {
      log.error("Multiple part HTML email is not supporting");
    } catch (IOException e) {
      log.error("Can not get email template from path: " + VERIFICATION_TEMPLATE_EMAIL_PATH);
    } catch (TemplateException e) {
      log.error("Can not get parse email template at: " + VERIFICATION_TEMPLATE_EMAIL_PATH);
    }
  }

  @Override
  public void sendResetPasswordEmail(UUID uid, String baseUrl, String redirectUrl) {
    var recipientAccount = accountRepository.findByUid(uid);
    if (recipientAccount.isEmpty()) {
      log.error("Can not find email recipient with uuid: " + uid);
      return;
    }

    EmailDetail emailDetail = EmailDetail.builder()
        .sender(DEFAULT_SENDER)
        .recipient(getRecipientEmail(recipientAccount.get()))
        .subject("Reset Password")
        .contentTemplatePath(RESET_PASSWORD_TEMPLATE_EMAIL_PATH)
        .build();

    var token = keycloakService.getEmailToken(uid, EMAIL_ACTION.UPDATE_PASSWORD, redirectUrl).orElse("");
    String url = UriComponentsBuilder
        .fromHttpUrl(baseUrl)
        .queryParam("key", token)
        .queryParam("redirectUrl", redirectUrl)
        .toUriString();
    emailDetail.setVerificationUrl(url);
    emailDetail.setRecipientName(recipientAccount.get().getDisplayName());

    try {
      sendEmail(emailDetail);
    } catch (MessagingException e) {
      log.error("Multiple part HTML email is not supporting");
    } catch (IOException e) {
      log.error("Can not get email template from path: " + RESET_PASSWORD_TEMPLATE_EMAIL_PATH);
    } catch (TemplateException e) {
      log.error("Can not get parse email template at: " + RESET_PASSWORD_TEMPLATE_EMAIL_PATH);
    }
  }

  @Override
  public void processVerificationEmail(String token) {
    try {
      keycloakService.triggerEmailVerification(token);
    } catch (IOException e) {
      log.error("Can not processing email verification from token: " + token);
    }
  }

  @Override
  public void processResetPasswordEmail(String token, CredentialDto credentialDto) {
    try {
      keycloakService.triggerPasswordReset(token, credentialDto);
    } catch (IOException e) {
      log.error("Can not processing password reset from token: " + token);
    } catch (KeycloakUnauthorizedException e) {
      log.error("Can not verify talent account in keycloak server", e);
    }
  }

  private void sendEmail(EmailDetail emailDetail) throws MessagingException, IOException, TemplateException {
    MimeMessage message = mailSender.createMimeMessage();
    MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(message,
        MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
        StandardCharsets.UTF_8.name());

    message.setFrom(emailDetail.getSender());
    mimeMessageHelper.setTo(emailDetail.getRecipient());
    mimeMessageHelper.setSubject(emailDetail.getSubject());

    // Setup message body
    Template freemarkerTemplate = freemarkerConfigurer.getConfiguration()
        .getTemplate(emailDetail.getContentTemplatePath());
    String htmlBody = FreeMarkerTemplateUtils.processTemplateIntoString(freemarkerTemplate, emailDetail.getContext());

    MimeMultipart multipart = new MimeMultipart();
    BodyPart messageBodyPart = new MimeBodyPart();
    messageBodyPart.setContent(htmlBody, "text/html");
    multipart.addBodyPart(messageBodyPart);
    message.setContent(multipart);

    mailSender.send(message);
  }

  private String getRecipientEmail(Account account) {
    if (account instanceof Organizer) {
      return ((Organizer) account).getOrganizerDetail().getEmail();
    } else if (account instanceof Talent) {
      return ((Talent) account).getTalentDetail().getEmail();
    } else {
      log.error("Can not detect the user type of recipient with uid " + account.getUid());
      return "";
    }
  }
}
