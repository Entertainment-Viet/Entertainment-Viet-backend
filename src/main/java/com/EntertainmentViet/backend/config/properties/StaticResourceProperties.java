package com.EntertainmentViet.backend.config.properties;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("static-resource")
@Getter
@Setter
@NoArgsConstructor
public class StaticResourceProperties {
  private String defaultAvatar;
}
