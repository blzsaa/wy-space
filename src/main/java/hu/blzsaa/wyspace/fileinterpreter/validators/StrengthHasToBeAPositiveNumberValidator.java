package hu.blzsaa.wyspace.fileinterpreter.validators;

import hu.blzsaa.wyspace.fileinterpreter.FileLineDto;
import hu.blzsaa.wyspace.fileinterpreter.constraints.StrengthHasToBeAPositiveNumber;
import java.util.Objects;
import java.util.regex.Pattern;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class StrengthHasToBeAPositiveNumberValidator
    implements ConstraintValidator<StrengthHasToBeAPositiveNumber, FileLineDto> {

  @Override
  public boolean isValid(FileLineDto dto, ConstraintValidatorContext cxt) {
    if (dto == null) {
      return true;
    }
    if (dto.fields().size() < 4) {
      return true;
    }
    String strength = dto.fields().get(dto.fields().size() - 3);
    return Pattern.matches("\\d+", strength) && !Objects.equals(strength, "0");
  }
}
