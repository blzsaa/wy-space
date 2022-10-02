package hu.blzsaa.wyspace.fileinterpreter.validators;

import hu.blzsaa.wyspace.fileinterpreter.FileLineDto;
import hu.blzsaa.wyspace.fileinterpreter.constraints.StartTimeHasToBeValidTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class StartTimeHasToBeValidTimeValidator
    implements ConstraintValidator<StartTimeHasToBeValidTime, FileLineDto> {

  @Override
  public boolean isValid(FileLineDto dto, ConstraintValidatorContext cxt) {
    if (dto == null) {
      return true;
    }
    if (dto.fields().size() < 4) {
      return true;
    }
    try {
      LocalTime.parse(
          dto.fields().get(dto.fields().size() - 2), DateTimeFormatter.ofPattern("HH:mm"));
      return true;
    } catch (Exception e) {
      return false;
    }
  }
}
