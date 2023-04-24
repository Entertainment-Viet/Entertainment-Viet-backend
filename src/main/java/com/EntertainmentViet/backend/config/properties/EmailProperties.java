package com.EntertainmentViet.backend.config.properties;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
@Setter
@NoArgsConstructor
public class EmailProperties {

  @Value("${spring.mail.username}")
  private String sender;
}
