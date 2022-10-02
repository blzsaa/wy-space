package hu.blzsaa.wyspace.fileinterpreter;

import java.util.List;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class FileLineDtoTest {
  private static final String VALID_TIME1 = "12:30";
  private static final String VALID_TIME_2 = "12:31";
  private Validator validator;

  @BeforeEach
  void setUp() {
    validator = Validation.buildDefaultValidatorFactory().getValidator();
  }

  @Test
  void emptyLineShouldBeInvalid() {
    // given
    FileLineDto fileLineDto = new FileLineDto(1, List.of());

    // when
    Set<ConstraintViolation<FileLineDto>> actual = validator.validate(fileLineDto);

    // then
    Assertions.assertThat(actual)
        .extracting("interpolatedMessage")
        .containsOnly(
            "Invalid line: FileLineDto[lineNumber=1, fields=[]], line should have at least 3 commas");
  }

  @Test
  void whenFieldsAreMissingValidatorsShouldNotThrowException() {
    // given
    FileLineDto fileLineDto = new FileLineDto(1, List.of("name"));

    // when
    Set<ConstraintViolation<FileLineDto>> actual = validator.validate(fileLineDto);

    // then
    Assertions.assertThat(actual)
        .extracting("interpolatedMessage")
        .containsOnly(
            "Invalid line: FileLineDto[lineNumber=1, fields=[name]], line should have at least 3 commas");
  }

  @Test
  void whenValidationFailsItShouldWriteOutWhichLineIsInvalid() {
    // given
    FileLineDto fileLineDto = new FileLineDto(10, List.of("name"));

    // when
    Set<ConstraintViolation<FileLineDto>> actual = validator.validate(fileLineDto);

    // then
    Assertions.assertThat(actual)
        .extracting("interpolatedMessage")
        .containsOnly(
            "Invalid line: FileLineDto[lineNumber=10, fields=[name]], line should have at least 3 commas");
  }

  @ValueSource(strings = {"-3", "-1", "0", "not-a-number"})
  @ParameterizedTest
  void strengthHasToBeAPositiveNumber(String strength) {
    // given
    FileLineDto fileLineDto =
        new FileLineDto(1, List.of("name", strength, VALID_TIME1, VALID_TIME_2));

    // when
    Set<ConstraintViolation<FileLineDto>> actual = validator.validate(fileLineDto);

    // then
    Assertions.assertThat(actual)
        .extracting("interpolatedMessage")
        .containsOnly(
            "Invalid line: FileLineDto[lineNumber=1, fields=[name, %s, 12:30, 12:31]], strength has to be a positive number"
                .formatted(strength));
  }

  @ValueSource(strings = {"1230", "1261", "2400", "not-a-time"})
  @ParameterizedTest
  void startTimeHasToFollowHHmmPattern(String startTime) {
    // given
    FileLineDto fileLineDto = new FileLineDto(1, List.of("name", "1", startTime, "12:01"));

    // when
    Set<ConstraintViolation<FileLineDto>> actual = validator.validate(fileLineDto);

    // then
    Assertions.assertThat(actual)
        .extracting("interpolatedMessage")
        .containsOnly(
            "Invalid line: FileLineDto[lineNumber=1, fields=[name, 1, %s, 12:01]], start time is not valid, valid time format is HH:mm"
                .formatted(startTime));
  }

  @ValueSource(strings = {"1230", "1261", "2400", "not-a-time"})
  @ParameterizedTest
  void endTimeHasToFollowHHmmPattern(String endTime) {
    // given
    FileLineDto fileLineDto = new FileLineDto(1, List.of("name", "1", VALID_TIME1, endTime));

    // when
    Set<ConstraintViolation<FileLineDto>> actual = validator.validate(fileLineDto);

    // then
    Assertions.assertThat(actual)
        .extracting("interpolatedMessage")
        .containsOnly(
            "Invalid line: FileLineDto[lineNumber=1, fields=[name, 1, 12:30, %s]], end time is not valid, valid time format is HH:mm"
                .formatted(endTime));
  }

  @Test
  void validLineShouldCountAsValid() {
    // given
    FileLineDto fileLineDto = new FileLineDto(1, List.of("name", "1", VALID_TIME1, "12:31"));

    // when
    Set<ConstraintViolation<FileLineDto>> actual = validator.validate(fileLineDto);

    // then
    Assertions.assertThat(actual).isEmpty();
  }

  @Test
  void validLineShouldCountAsValidEvenWHenNameHasMultipleCommasInIt() {
    // given
    FileLineDto fileLineDto = new FileLineDto(1, List.of("n,,,,ame", "1", VALID_TIME1, "12:31"));

    // when
    Set<ConstraintViolation<FileLineDto>> actual = validator.validate(fileLineDto);

    // then
    Assertions.assertThat(actual).isEmpty();
  }
}
