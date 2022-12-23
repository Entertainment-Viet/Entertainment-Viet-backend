package com.EntertainmentViet.backend.domain.entities;

import com.EntertainmentViet.backend.domain.standardTypes.AccountType;
import com.vladmihalcea.hibernate.type.basic.PostgreSQLEnumType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Inheritance(strategy = InheritanceType.JOINED)
@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@TypeDef(
    name = "pgsql_enum",
    typeClass = PostgreSQLEnumType.class
)
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
public abstract class Account extends Identifiable {

  @Id
  @GeneratedValue
  private Long id;

  @NotBlank
  private String displayName;

  @Enumerated(EnumType.STRING)
  @Column(columnDefinition = "account_type")
  @Type( type = "pgsql_enum" )
  private AccountType accountType;
}
