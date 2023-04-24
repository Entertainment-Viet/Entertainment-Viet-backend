package com.EntertainmentViet.backend.domain.entities;

import com.EntertainmentViet.backend.domain.entities.message.Conversation;
import com.EntertainmentViet.backend.domain.entities.message.Conversation_;
import com.EntertainmentViet.backend.domain.standardTypes.AccountType;
import com.querydsl.core.annotations.QueryInit;
import com.vladmihalcea.hibernate.type.array.ListArrayType;
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
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Set;

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
@TypeDef(name = "list-array",typeClass = ListArrayType.class)
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
public abstract class Account extends Identifiable implements Searchable {

  @Id
  @GeneratedValue
  private Long id;

  @NotBlank
  private String displayName;

  @Enumerated(EnumType.STRING)
  @Column(columnDefinition = "account_type")
  @Type( type = "pgsql_enum" )
  private AccountType accountType;

  @ManyToMany(mappedBy = Conversation_.PARTICIPANT)
  @QueryInit("*.*")
  private Set<Conversation> conversations;

  @Type(type = "list-array")
  @Column(
      name = "hash_tag",
      columnDefinition = "text[]"
  )
  @NotNull
  private List<String> hashTag;

  private String email;
}
