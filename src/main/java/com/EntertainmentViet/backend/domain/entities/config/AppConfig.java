package com.EntertainmentViet.backend.domain.entities.config;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class AppConfig {

  @Id
  @GeneratedValue
  private Long id;

  private String propertyName;

  private String propertyValue;
}
