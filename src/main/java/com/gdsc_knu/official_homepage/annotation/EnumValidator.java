package com.gdsc_knu.official_homepage.annotation;

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
        Enum<?>[] enumValues = this.enumClass.getEnumConstants();
        if (value instanceof String) {
            for (Enum<?> enumValue : enumValues) {
                if (value.equals(enumValue.toString())) {
                    return true;
                }
            }
        }
        else if (value instanceof Enum) {
            for (Enum<?> enumValue : enumValues) {
                if (value.equals(enumValue)) {
                    return true;
                }
            }
        }
        return false;
    }
}
