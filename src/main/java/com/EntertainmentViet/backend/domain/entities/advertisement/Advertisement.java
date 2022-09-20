package com.EntertainmentViet.backend.domain.entities.advertisement;

import com.EntertainmentViet.backend.domain.entities.Identifiable;
import com.EntertainmentViet.backend.domain.values.Price;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotNull;
import java.time.OffsetDateTime;

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
  private OffsetDateTime expiredTime;

  @NotNull
  private Integer priority;

  public Price calculateFee(OffsetDateTime newExpiredTime) {
    return null;
  }

  public void cancel() {
    setExpiredTime(OffsetDateTime.now());
  }

  public void extendTime(OffsetDateTime newExpiredTime) {
    var requiredFee = calculateFee(newExpiredTime);
    // TODO check if fee is paid yet
    setExpiredTime(newExpiredTime);
  }
}
