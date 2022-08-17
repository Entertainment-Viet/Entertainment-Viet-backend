package com.EntertainmentViet.backend.domain.values;

import com.EntertainmentViet.backend.domain.standardTypes.Currency;
import com.vladmihalcea.hibernate.type.basic.PostgreSQLEnumType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
@TypeDef(
    name = "pgsql_enum",
    typeClass = PostgreSQLEnumType.class
)
public class Price implements Serializable {

  private Double min;

  private Double max;

  @Enumerated(EnumType.STRING)
  @Column(columnDefinition = "currency_type")
  @Type( type = "pgsql_enum" )
  @NotNull
  private Currency currency;

  public boolean checkIfFixedPrice() {
    return min.equals(max);
  }
}
