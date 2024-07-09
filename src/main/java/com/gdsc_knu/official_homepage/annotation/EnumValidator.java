package com.gdsc_knu.official_homepage.annotation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class EnumValidator implements ConstraintValidator<ValidEnum, String> {
    private ValidEnum validEnum;
    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        boolean result = false;
        Object[] enumValues = this.validEnum.enumClass().getEnumConstants();
        if (enumValues != null) {
            for (Object enumValue : enumValues) {
                if (s.equals(enumValue.toString())) {
                    result = true;
                    break;
                }
            }
        }
        return result;
    }

    @Override
    public void initialize(ValidEnum constraintAnnotation) {
        this.validEnum = constraintAnnotation;
    }
}
