package com.EntertainmentViet.backend.domain.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = PriceValidator.class)
@Target( { ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface PriceConstraint {
  String message() default "Invalid price";
  Class<?>[] groups() default {};
  Class<? extends Payload>[] payload() default {};
}
