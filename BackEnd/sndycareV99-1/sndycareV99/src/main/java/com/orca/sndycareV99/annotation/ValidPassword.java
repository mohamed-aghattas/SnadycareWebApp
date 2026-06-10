package com.orca.sndycareV99.annotation;
import com.orca.sndycareV99.annotation.PasswordConstraintValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = PasswordConstraintValidator.class)
@Target({ElementType.FIELD , ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidPassword {

    String message = "Weak password! It must contain at least " +
            "one uppercase letter, one lowercase letter, one number, " +
            "and one special character, and be at least 8 characters long";

    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}