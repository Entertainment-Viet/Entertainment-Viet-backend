package com.EntertainmentViet.backend.features.email.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.HashMap;
import java.util.Map;

@NoArgsConstructor
@SuperBuilder
@Getter
@Setter
public class EmailDetail {
  private String sender;
  private String recipient;
  private String subject;
  private String contentTemplatePath;
  @Builder.Default
  private Map<String, Object> context = new HashMap<>();

  public void addContext(String key, Object value) {
    if (key != null) {
      context.put(key, value);
    }
  }

  public void setRecipientName(String recipientName) {
    addContext("recipientName", recipientName);
  }

  public void setVerificationUrl(String url) {
    addContext("verificationUrl", url);
  }
}
