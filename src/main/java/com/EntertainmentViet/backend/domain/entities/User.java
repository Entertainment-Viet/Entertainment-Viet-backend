package com.EntertainmentViet.backend.domain.entities;

import com.EntertainmentViet.backend.domain.businessLogic.AuditableListener;
import com.EntertainmentViet.backend.domain.standardTypes.AccountType;
import com.EntertainmentViet.backend.domain.standardTypes.UserState;
import com.vladmihalcea.hibernate.type.basic.PostgreSQLEnumType;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
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
import java.time.OffsetDateTime;

@MappedSuperclass
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@TypeDef(
    name = "pgsql_enum",
    typeClass = PostgreSQLEnumType.class
)
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@EntityListeners({AuditableListener.class})
public abstract class  User extends Account implements Auditable {

  private OffsetDateTime createdAt;

  @Enumerated(EnumType.STRING)
  @Column(columnDefinition = "user_state")
  @Type( type = "pgsql_enum" )
  @NotNull
  private UserState userState;

  @Enumerated(EnumType.STRING)
  @Column(columnDefinition = "account_type")
  @Type( type = "pgsql_enum" )
  private AccountType accountType;

  private Boolean archived;

  public boolean sendVerifyRequest() {
    if (!userState.equals(UserState.GUEST)) {
      return false;
    }
    if (!checkIfUserVerifiable()) {
      return false;
    }
    setUserState(UserState.PENDING);
    return true;
  }

  public boolean verifyAccount() {
    if (!getUserState().equals(UserState.PENDING) && !getUserState().equals(UserState.UNVERIFIED)) {
      return false;
    }
    if (!checkIfUserVerifiable()) {
      return false;
    }
    setUserState(UserState.VERIFIED);
    return true;
  }

  public boolean verifyPayment() {
    if (userState.equals(UserState.VERIFIED)) {
      userState = UserState.CHARGEABLE;
      return true;
    }
    return false;
  }

  public boolean archive() {
    userState = UserState.ARCHIVED;
    return true;
  }

  protected abstract boolean checkIfUserVerifiable();
}
