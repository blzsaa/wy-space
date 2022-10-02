package hu.blzsaa.wyspace.fileinterpreter.constraints;

import hu.blzsaa.wyspace.fileinterpreter.validators.FieldSeparatorValidator;
import java.lang.annotation.*;
import javax.validation.Constraint;
import javax.validation.Payload;

@Documented
@Constraint(validatedBy = FieldSeparatorValidator.class)
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface FieldSeparatorConstraint {
  String message() default "Invalid line: ${validatedValue}, line should have at least 3 commas";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
