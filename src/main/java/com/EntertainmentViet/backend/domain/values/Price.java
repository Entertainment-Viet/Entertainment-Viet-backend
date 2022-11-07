package com.EntertainmentViet.backend.domain.values;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;

import com.EntertainmentViet.backend.domain.standardTypes.Currency;
import com.EntertainmentViet.backend.domain.validator.PriceConstraint;
import com.vladmihalcea.hibernate.type.basic.PostgreSQLEnumType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
@TypeDef(
    name = "pgsql_enum",
    typeClass = PostgreSQLEnumType.class
)
@PriceConstraint
public class Price implements Serializable {

  private Double min;

  private Double max;

  @Enumerated(EnumType.STRING)
  @Column(columnDefinition = "currency_type")
  @Type(type = "pgsql_enum")
  @NotNull
  private Currency currency;

  public Price updateInfo(Price newData) {
    if (!checkIfNewValueValid(newData)) {
      return this;
    }

    if (newData.getMax() != null) {
      if (currency.equals(newData.getCurrency())) {
        setMax(newData.getMax());
      }
    }
    if (newData.getMin() != null) {
      if (currency.equals(newData.getCurrency())) {
        setMin(newData.getMin());
      }
    }
    if (newData.getCurrency() != null) {
      // TODO: convert currency
      setCurrency(newData.getCurrency());
    }
    return this;
  }

  public boolean checkIfFixedPrice() {
    return min.equals(max);
  }

  public boolean checkIfValid() {
    return min <= max;
  }

  private boolean checkIfNewValueValid(Price data) {
    if (data.getMin() == null && data.getMax() == null) {
      return true;
    }
    if (data.getMin() == null && min > data.getMax()) {
      return false;
    }
    if (data.getMax() == null && max < data.getMin()) {
      return false;
    }
    return true;
  }
}
