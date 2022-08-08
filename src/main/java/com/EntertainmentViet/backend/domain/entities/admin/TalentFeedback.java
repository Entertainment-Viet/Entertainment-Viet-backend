package com.EntertainmentViet.backend.domain.entities.admin;

import com.EntertainmentViet.backend.domain.entities.talent.Talent;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

@SuperBuilder
@NoArgsConstructor
@Getter
@Setter
@Entity
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
public class TalentFeedback extends Feedback<Talent> {

  @ManyToOne(fetch = FetchType.LAZY)
  @NotNull
  private Talent talent;
}
