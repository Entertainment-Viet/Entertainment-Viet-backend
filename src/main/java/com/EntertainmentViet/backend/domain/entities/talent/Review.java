package com.EntertainmentViet.backend.domain.entities.talent;

import com.EntertainmentViet.backend.domain.businessLogic.AuditableListener;
import com.EntertainmentViet.backend.domain.entities.Auditable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.Instant;

@SuperBuilder
@NoArgsConstructor
@Getter
@Setter
@Entity
@EntityListeners({AuditableListener.class})
public class Review implements Auditable, Serializable{

  @Id
  @GeneratedValue
  private Long id;

  private Instant createdAt;

  @ManyToOne(fetch = FetchType.LAZY)
  @NotNull
  private Talent talent;

  // TODO: Maybe change this to entity since there can be multiple translation we require user to input
  private String comment;

  @Min(0)
  @Max(10)
  @NotNull
  private Integer score;
}
