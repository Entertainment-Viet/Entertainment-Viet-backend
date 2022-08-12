package com.EntertainmentViet.backend.domain.entities.booking;

import com.EntertainmentViet.backend.domain.standardTypes.WorkType;
import com.EntertainmentViet.backend.domain.values.Category;
import com.EntertainmentViet.backend.domain.values.Price;
import com.fasterxml.jackson.databind.JsonNode;
import com.vladmihalcea.hibernate.type.basic.PostgreSQLEnumType;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.Duration;
import java.time.Instant;

@SuperBuilder
@NoArgsConstructor
@Getter
@Setter
@Entity
@TypeDef(
    name = "pgsql_enum",
    typeClass = PostgreSQLEnumType.class
)
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
public class JobDetail implements Serializable {

  @Id
  @GeneratedValue
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "category_id")
  private Category category;

  @Enumerated(EnumType.STRING)
  @Column(columnDefinition = "work_type")
  @Type( type = "pgsql_enum" )
  @NotNull
  private WorkType workType;

  @Embedded
  @NotNull
  private Price price;

  @NotNull
  private Duration performanceDuration;

  @NotNull
  private Instant performanceTime;

  private String note;

  @Type(type = "jsonb")
  @Column(columnDefinition = "jsonb")
  private JsonNode extensions;

  public void addCategory(Category category) {
    setCategory(category);
  }

  public void removeCategory() {
    setCategory(null);
  }
}
