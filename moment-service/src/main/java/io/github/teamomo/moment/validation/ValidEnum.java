package io.github.teamomo.moment.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

//todo: check error messages in exceptionHandling
@Constraint(validatedBy = EnumValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidEnum {
  String message() default "Invalid value for enum";
  Class<?>[] groups() default {};
  Class<? extends Payload>[] payload() default {};
  Class<? extends Enum<?>> enumClass();
}
