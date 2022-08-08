package com.EntertainmentViet.backend.domain.entities.advertisement;

import com.EntertainmentViet.backend.domain.entities.Identifiable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotNull;
import java.time.Instant;

@MappedSuperclass
@SuperBuilder
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
public abstract class Advertisement<T extends Advertisable> extends Identifiable {

  @Id
  @GeneratedValue
  private Long id;

  @NotNull
  private Instant expiredTime;

  @NotNull
  private Integer priority;

}
