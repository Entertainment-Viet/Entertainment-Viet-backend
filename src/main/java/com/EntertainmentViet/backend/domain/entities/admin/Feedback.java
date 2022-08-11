package com.EntertainmentViet.backend.domain.entities.admin;

import com.EntertainmentViet.backend.domain.businessLogic.AuditableListener;
import com.EntertainmentViet.backend.domain.entities.Auditable;
import com.EntertainmentViet.backend.domain.entities.Identifiable;
import com.EntertainmentViet.backend.domain.entities.User;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.time.Instant;

@MappedSuperclass
@SuperBuilder
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@EntityListeners({AuditableListener.class})
public abstract class Feedback<T extends User> extends Identifiable implements Auditable {

  @Id
  @GeneratedValue
  private Long id;

  private Instant createdAt;

  private String content;

  @ManyToOne(fetch = FetchType.LAZY)
  private Admin admin;
}
