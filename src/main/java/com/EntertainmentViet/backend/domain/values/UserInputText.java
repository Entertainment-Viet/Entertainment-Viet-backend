package com.EntertainmentViet.backend.domain.values;

import com.EntertainmentViet.backend.domain.standardTypes.SupportLanguage;
import com.fasterxml.jackson.databind.JsonNode;
import com.vladmihalcea.hibernate.type.basic.PostgreSQLEnumType;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Embeddable
@TypeDef(
    name = "pgsql_enum",
    typeClass = PostgreSQLEnumType.class
)
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
public class UserInputText implements Serializable {

  @Enumerated(EnumType.STRING)
  @Column(columnDefinition = "support_language")
  @Type( type = "pgsql_enum" )
  @NotNull
  private SupportLanguage inputLang;

  private String rawInput;

  @Type(type = "jsonb")
  @Column(columnDefinition = "jsonb")
  private JsonNode inputTranslation;
}
