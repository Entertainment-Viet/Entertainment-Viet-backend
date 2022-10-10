package com.EntertainmentViet.backend.domain.entities.talent;

import com.EntertainmentViet.backend.domain.businessLogic.AuditableListener;
import com.EntertainmentViet.backend.domain.entities.Auditable;
import com.EntertainmentViet.backend.domain.entities.Identifiable;
import com.EntertainmentViet.backend.domain.entities.organizer.Organizer;
import com.EntertainmentViet.backend.domain.values.UserInputText;
import com.EntertainmentViet.backend.domain.values.UserInputText_;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.OffsetDateTime;

@SuperBuilder
@NoArgsConstructor
@Getter
@Setter
@Entity
@EntityListeners({AuditableListener.class})
public class Review extends Identifiable implements Auditable, Serializable{

  @Id
  @GeneratedValue
  private Long id;

  private OffsetDateTime createdAt;

  @ManyToOne(fetch = FetchType.LAZY)
  @NotNull
  private Talent talent;

  @ManyToOne(fetch = FetchType.LAZY)
  @NotNull
  private Organizer organizer;

  @Embedded
  @AttributeOverrides({
      @AttributeOverride( name = UserInputText_.INPUT_LANG, column = @Column(name = Review_.COMMENT + "_" + UserInputText_.INPUT_LANG)),
      @AttributeOverride( name = UserInputText_.RAW_INPUT, column = @Column(name = Review_.COMMENT + "_" + UserInputText_.RAW_INPUT)),
      @AttributeOverride( name = UserInputText_.INPUT_TRANSLATION, column = @Column(name = Review_.COMMENT + "_" + UserInputText_.INPUT_TRANSLATION))
  })
  private UserInputText comment;

  @Min(1)
  @Max(5)
  @NotNull
  private Integer score;
}
