package hu.blzsaa.wyspace.fileinterpreter.constraints;

import hu.blzsaa.wyspace.fileinterpreter.validators.StartTimeHasToBeValidTimeValidator;
import java.lang.annotation.*;
import javax.validation.Constraint;
import javax.validation.Payload;

@Documented
@Constraint(validatedBy = StartTimeHasToBeValidTimeValidator.class)
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface StartTimeHasToBeValidTime {
  String message() default
      "Invalid line: ${validatedValue}, start time is not valid, valid time format is HH:mm";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
