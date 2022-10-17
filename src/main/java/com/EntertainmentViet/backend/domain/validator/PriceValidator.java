package com.EntertainmentViet.backend.domain.validator;

import com.EntertainmentViet.backend.domain.values.Price;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PriceValidator implements ConstraintValidator<PriceConstraint, Price> {
  @Override
  public boolean isValid(Price price, ConstraintValidatorContext context) {
    return price.getMin() <= price.getMax();
  }
}
