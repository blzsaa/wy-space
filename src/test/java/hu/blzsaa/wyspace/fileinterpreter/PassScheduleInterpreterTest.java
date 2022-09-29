package hu.blzsaa.wyspace.fileinterpreter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

import hu.blzsaa.wyspace.PassDtoBuilder;
import java.io.BufferedReader;
import java.io.StringReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.supercsv.exception.SuperCsvConstraintViolationException;

class PassScheduleInterpreterTest {
  private PassScheduleInterpreter underTest;

  @BeforeEach
  void setUp() {
    underTest = new PassScheduleInterpreter();
  }

  @Test
  void readInputShouldParseEmptyFileToEmptyList() {
    // given
    BufferedReader reader = new BufferedReader(new StringReader(""));

    // when
    var actual = underTest.readInput(reader);

    // then
    assertThat(actual).isEmpty();
  }

  @Test
  void readInputShouldParseOneLine() {
    // given
    BufferedReader reader = new BufferedReader(new StringReader("RedDwarf,2,00:00,00:30"));

    // when
    var actual = underTest.readInput(reader);

    // then
    assertThat(actual)
        .containsOnly(
            new PassDtoBuilder().name("RedDwarf").strength(2).startTime(0).endTime(1).build());
  }

  @Test
  void readInputShouldIgnoreNewLineAtTheEndOfTheFile() {
    // given
    BufferedReader reader = new BufferedReader(new StringReader("RedDwarf,2,00:00,00:30\n"));

    // when
    var actual = underTest.readInput(reader);

    // then
    assertThat(actual)
        .containsOnly(
            new PassDtoBuilder().name("RedDwarf").strength(2).startTime(0).endTime(1).build());
  }

  @Test
  void readInputShouldParseOneLineEvenIfNameContainsComma() {
    // given
    BufferedReader reader = new BufferedReader(new StringReader("\",\"RedDwarf,2,00:00,00:30"));

    // when
    var actual = underTest.readInput(reader);

    // then
    assertThat(actual)
        .containsOnly(
            new PassDtoBuilder().name(",RedDwarf").strength(2).startTime(0).endTime(1).build());
  }

  @Test
  void readInputShouldParseMultipleLines() {
    // given
    String content = String.join("\n", "RedDwarf,2,00:00,00:30", "RedDwarf2,7,04:30,12:30");
    BufferedReader reader = new BufferedReader(new StringReader(content));

    // when
    var actual = underTest.readInput(reader);

    // then
    assertThat(actual)
        .containsOnly(
            new PassDtoBuilder().name("RedDwarf").strength(2).startTime(0).endTime(1).build(),
            new PassDtoBuilder().name("RedDwarf2").strength(7).startTime(9).endTime(25).build());
  }

  @ParameterizedTest
  @ValueSource(
      strings = {
        ",2,00:00,00:30",
        "RedDwarf,,00:00,00:30",
        "RedDwarf,2,,00:30",
        "RedDwarf,2,00:00,",
      })
  void allFieldsShouldBePresent(String line) {
    // given
    BufferedReader reader = new BufferedReader(new StringReader(line));

    // when
    var actual = catchThrowable(() -> underTest.readInput(reader));

    // then
    assertThat(actual)
        .isInstanceOf(SuperCsvConstraintViolationException.class)
        .hasMessageContaining("null value encountered");
  }

  @ParameterizedTest
  @ValueSource(
      strings = {"RedDwarf,2,0000,00:30", "RedDwarf,2,00:00,0030", "RedDwarf,2,00:00,0A30"})
  void startTimeAndEndTimeShouldBeTwoNumberColonAndToNumbersFormat(String line) {
    // given
    BufferedReader reader = new BufferedReader(new StringReader(line));

    // when
    var actual = catchThrowable(() -> underTest.readInput(reader));

    // then
    assertThat(actual)
        .isInstanceOf(SuperCsvConstraintViolationException.class)
        .hasMessageContaining("does not match the regular expression '\\d\\d:\\d\\d'");
  }
}
