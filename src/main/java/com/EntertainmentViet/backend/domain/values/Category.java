package com.EntertainmentViet.backend.domain.values;

import com.EntertainmentViet.backend.domain.entities.Identifiable;
import com.vladmihalcea.hibernate.type.basic.PostgreSQLEnumType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.TypeDef;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@SuperBuilder
@NoArgsConstructor
@Getter
@Setter
@Entity
@TypeDef(
    name = "pgsql_enum",
    typeClass = PostgreSQLEnumType.class
)
public class Category extends Identifiable implements Serializable {

  @Id
  @GeneratedValue
  private Long id;

  @NotBlank
  private String name;

  @ManyToOne(fetch = FetchType.LAZY)
  private Category parent;
}
