package com.EntertainmentViet.backend.domain.entities.talent;

import com.vladmihalcea.hibernate.type.array.ListArrayType;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@TypeDef(name = "list-array",typeClass = ListArrayType.class)
public class PriorityScore {

  @Id
  @GeneratedValue
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @NotNull
  private Talent talent;

  @ManyToOne(fetch = FetchType.LAZY)
  @OnDelete(action = OnDeleteAction.CASCADE)
  @NotNull
  private ScoreType scoreType;

  @Type(type = "list-array")
  @Column(
      name = "proof",
      columnDefinition = "text[]"
  )
  private List<String> proof;

  @NotNull
  private Boolean approved;

  @NotNull
  private String achievement;

  public Double calculateScore() {
    if (!approved) {
      return 0.0;
    }
    return scoreType.getRate();
  }

  public boolean checkIfSongScore() {
    if (scoreType.getId().equals(ScoreType.SONG_SCORE_TYPE_ID)) {
      return true;
    }
    return false;
  }
}
