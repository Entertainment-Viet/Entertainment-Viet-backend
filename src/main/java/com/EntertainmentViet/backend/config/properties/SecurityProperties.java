package com.EntertainmentViet.backend.config.properties;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
@ConfigurationProperties("security")
@Getter
@Setter
@NoArgsConstructor
public class SecurityProperties {
  private List<String> allowedOrigins = new ArrayList<>();
}
