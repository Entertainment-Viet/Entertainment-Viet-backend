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
public class AwsProperties {

  @Value("${cloud.aws.credentials.accessKey}")
  private String awsId;

  @Value("${cloud.aws.credentials.secretKey}")
  private String awsKey;

  @Value("${cloud.aws.region.static}")
  private String region;

  @Value("${amazonProperties.bucketName}")
  private String bucketName;
}
