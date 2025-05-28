package io.github.teamomo.momentswebapp.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;


public class EnumValidator implements ConstraintValidator<ValidEnum, Object> {

  private Class<? extends Enum<?>> enumClass;

  @Override
  public void initialize(ValidEnum constraintAnnotation) {
    this.enumClass = constraintAnnotation.enumClass();
  }

  @Override
  public boolean isValid(Object value, ConstraintValidatorContext context) {
    if (value == null) {
      return true;
    }

    if (value instanceof String) {
      for (Enum<?> enumConstant : enumClass.getEnumConstants()) {
        if (enumConstant.name().equals(value)) {
          return true;
        }
      }
    } else if (enumClass.isInstance(value)) {
      return true;
    }
    return false;
  }
}
