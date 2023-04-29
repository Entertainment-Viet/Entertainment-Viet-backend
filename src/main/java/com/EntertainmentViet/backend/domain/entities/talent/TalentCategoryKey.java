package com.EntertainmentViet.backend.domain.entities.talent;

import lombok.*;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class TalentCategoryKey implements Serializable {

  private Long talentId;

  private Long categoryId;
}
