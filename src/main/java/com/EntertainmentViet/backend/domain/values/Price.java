package com.EntertainmentViet.backend.domain.values;

import com.EntertainmentViet.backend.domain.standardTypes.Currency;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Embeddable
public class Price implements Serializable {

  private Double min;

  private Double max;

  @Enumerated(EnumType.STRING)
  @Column(columnDefinition = "currency_type")
  @Type( type = "pgsql_enum" )
  @NotNull  private Currency currency;

}
