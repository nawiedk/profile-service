package com.nawiedk.profile.validation;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import java.util.Set;

import static jakarta.validation.Validation.buildDefaultValidatorFactory;

public class Validation {

    private static final Validator validator;

    static {
        try (ValidatorFactory factory = buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
    }

    public static <T> void validate(T target) {
        Set<ConstraintViolation<T>> violations = validator.validate(target);
        if (!violations.isEmpty()) {
            StringBuilder output = new StringBuilder("Validation failed: ");
            for (ConstraintViolation<T> violation : violations) {
                output.append(violation.getPropertyPath()).append(": ")
                        .append(violation.getMessage()).append("; ");
            }
            throw new IllegalArgumentException(output.toString());
        }
    }
}
