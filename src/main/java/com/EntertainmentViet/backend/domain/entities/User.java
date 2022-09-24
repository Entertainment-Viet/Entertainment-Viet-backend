package com.EntertainmentViet.backend.domain.entities;

import com.EntertainmentViet.backend.domain.businessLogic.AuditableListener;
import com.EntertainmentViet.backend.domain.standardTypes.UserState;
import com.EntertainmentViet.backend.domain.values.UserInputText;
import com.EntertainmentViet.backend.domain.values.UserInputText_;
import com.fasterxml.jackson.databind.JsonNode;
import com.vladmihalcea.hibernate.type.basic.PostgreSQLEnumType;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
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

  private String phoneNumber;

  private String email;

  private String address;

  @Embedded
  @AttributeOverrides({
      @AttributeOverride( name = UserInputText_.INPUT_LANG, column = @Column(name = User_.BIO + "_" + UserInputText_.INPUT_LANG)),
      @AttributeOverride( name = UserInputText_.RAW_INPUT, column = @Column(name = User_.BIO + "_" + UserInputText_.RAW_INPUT)),
      @AttributeOverride( name = UserInputText_.INPUT_TRANSLATION, column = @Column(name = User_.BIO + "_" + UserInputText_.INPUT_TRANSLATION))
  })
  private UserInputText bio;

  private OffsetDateTime createdAt;

  @Enumerated(EnumType.STRING)
  @Column(columnDefinition = "user_state")
  @Type( type = "pgsql_enum" )
  @NotNull
  private UserState userState;

  @Type(type = "jsonb")
  @Column(columnDefinition = "jsonb")
  private JsonNode extensions;

  public boolean verifyAccount() {
    if (userState.equals(UserState.GUEST)) {
      setUserState(UserState.VERIFIED);
      return true;
    }
    return false;
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
}
