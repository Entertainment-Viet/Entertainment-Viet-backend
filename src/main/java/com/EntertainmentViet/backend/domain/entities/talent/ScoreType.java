package com.EntertainmentViet.backend.domain.entities.talent;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
public class ScoreType {

  public static final Long SONG_SCORE_TYPE_ID = 1L;

  @Id
  @GeneratedValue
  private Long id;

  @NotNull
  private String name;

  @NotNull
  private Double rate;
}
