package com.EntertainmentViet.backend.domain.entities;

import com.EntertainmentViet.backend.domain.businessLogic.AuditableListener;
import com.EntertainmentViet.backend.domain.standardTypes.UserState;
import com.EntertainmentViet.backend.domain.standardTypes.UserType;
import com.vladmihalcea.hibernate.type.basic.PostgreSQLEnumType;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.OffsetDateTime;

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
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
@EntityListeners({AuditableListener.class})
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
public abstract class Users extends Account implements Auditable {

  private OffsetDateTime createdAt;

  @Enumerated(EnumType.STRING)
  @Column(columnDefinition = "user_state")
  @Type( type = "pgsql_enum" )
  @NotNull
  private UserState userState;

  @Enumerated(EnumType.STRING)
  @Column(columnDefinition = "account_type")
  @Type( type = "pgsql_enum" )
  private UserType userType;

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

  public void archive() {
    userState = UserState.ARCHIVED;
  }

  protected abstract boolean checkIfUserVerifiable();
}
