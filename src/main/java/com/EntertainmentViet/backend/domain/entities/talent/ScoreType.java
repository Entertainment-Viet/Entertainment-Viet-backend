package com.EntertainmentViet.backend.domain.entities.talent;

import com.EntertainmentViet.backend.domain.entities.Identifiable;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class ScoreType extends Identifiable {

  public static final Long SONG_SCORE_TYPE_ID = 1L;

  @Id
  @GeneratedValue
  private Long id;

  @NotNull
  private String name;

  @NotNull
  private Double rate;
}
