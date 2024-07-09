package com.gdsc_knu.official_homepage.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = {EnumValidator.class})
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidEnum {
    String message() default "올바르지 않은 값입니다.";
    Class<? extends Enum<?>> enumClass();

    Class<?>[] groups() default { };

    Class<?extends Payload>[] payload() default { };
}
