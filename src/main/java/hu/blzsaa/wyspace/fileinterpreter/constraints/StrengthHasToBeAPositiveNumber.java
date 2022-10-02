package hu.blzsaa.wyspace.fileinterpreter.constraints;

import hu.blzsaa.wyspace.fileinterpreter.validators.StrengthHasToBeAPositiveNumberValidator;
import java.lang.annotation.*;
import javax.validation.Constraint;
import javax.validation.Payload;

@Documented
@Constraint(validatedBy = StrengthHasToBeAPositiveNumberValidator.class)
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface StrengthHasToBeAPositiveNumber {
  String message() default "Invalid line: ${validatedValue}, strength has to be a positive number";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
