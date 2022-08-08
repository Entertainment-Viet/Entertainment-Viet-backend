package com.EntertainmentViet.backend.domain.entities.talent;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@SuperBuilder
@NoArgsConstructor
@Getter
@Setter
@Entity
public class Review implements Serializable {

  @Id
  @GeneratedValue
  private Long id;

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
