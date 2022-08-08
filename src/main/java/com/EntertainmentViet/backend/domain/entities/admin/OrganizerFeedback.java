package com.EntertainmentViet.backend.domain.entities.admin;

import com.EntertainmentViet.backend.domain.entities.organizer.Organizer;
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
public class OrganizerFeedback extends Feedback<Organizer> {

  @ManyToOne(fetch = FetchType.LAZY)
  @NotNull
  private Organizer organizer;
}
