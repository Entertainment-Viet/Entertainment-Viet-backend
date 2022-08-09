package com.EntertainmentViet.backend.domain.entities;

import com.EntertainmentViet.backend.domain.businessLogic.AuditableListener;
import com.EntertainmentViet.backend.domain.standardTypes.UserState;
import com.vladmihalcea.hibernate.type.basic.PostgreSQLEnumType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotNull;
import java.time.Instant;

@MappedSuperclass
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@TypeDef(
    name = "pgsql_enum",
    typeClass = PostgreSQLEnumType.class
)
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@EntityListeners({AuditableListener.class})
public abstract class User extends Account implements Auditable {

  private String phoneNumber;

  private String email;

  private String address;

  private String bio;

  private Instant createdAt;

  private String language;

  @Enumerated(EnumType.STRING)
  @Column(columnDefinition = "user_state")
  @Type( type = "pgsql_enum" )
  @NotNull
  private UserState userState;
}
