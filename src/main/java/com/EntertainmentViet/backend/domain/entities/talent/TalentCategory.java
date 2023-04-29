package com.EntertainmentViet.backend.domain.entities.talent;

import com.EntertainmentViet.backend.domain.values.Category;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.Objects;

@SuperBuilder
@NoArgsConstructor
@Getter
@Setter
@Entity
public class TalentCategory {

  @EmbeddedId
  private TalentCategoryKey id;

  @ManyToOne(fetch = FetchType.LAZY)
  @MapsId(TalentCategoryKey_.TALENT_ID)
  private Talent talent;

  @OneToOne(fetch = FetchType.LAZY)
  @MapsId(TalentCategoryKey_.CATEGORY_ID)
  private Category category;

  public TalentCategory(Talent talent, Category category) {
    this.talent = talent;
    this.category = category;
    this.id = new TalentCategoryKey(talent.getId(), category.getId());
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    TalentCategory that = (TalentCategory) o;
    return Objects.equals(id, that.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
}
