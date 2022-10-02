package hu.blzsaa.wyspace.fileinterpreter.validators;

import hu.blzsaa.wyspace.fileinterpreter.FileLineDto;
import hu.blzsaa.wyspace.fileinterpreter.constraints.FieldSeparatorConstraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class FieldSeparatorValidator
    implements ConstraintValidator<FieldSeparatorConstraint, FileLineDto> {

  @Override
  public boolean isValid(FileLineDto dto, ConstraintValidatorContext cxt) {

    if (dto == null) {
      return true;
    }
    return dto.fields().size() > 3;
  }
}
