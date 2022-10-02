package hu.blzsaa.wyspace.fileinterpreter.constraints;

import hu.blzsaa.wyspace.fileinterpreter.validators.EndTimeHasToBeValidTimeValidator;
import java.lang.annotation.*;
import javax.validation.Constraint;
import javax.validation.Payload;

@Documented
@Constraint(validatedBy = EndTimeHasToBeValidTimeValidator.class)
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface EndTimeHasToBeValidTime {
  String message() default
      "Invalid line: ${validatedValue}, end time is not valid, valid time format is HH:mm";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
