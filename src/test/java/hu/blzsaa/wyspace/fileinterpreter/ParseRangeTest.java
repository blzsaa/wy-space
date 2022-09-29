package hu.blzsaa.wyspace.fileinterpreter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.supercsv.exception.SuperCsvCellProcessorException;

class ParseRangeTest {

  private ParseRange underTest;

  @BeforeEach
  void setUp() {
    underTest = new ParseRange();
  }

  @Test
  void fieldShouldBeMandatory() {
    var actual = catchThrowable(() -> underTest.execute(null, null));

    // then
    assertThat(actual)
        .isInstanceOf(SuperCsvCellProcessorException.class)
        .hasMessageContaining(
            "this processor does not accept null input - if the column is optional then chain an Optional() processor before this one");
  }

  @Test
  void midnightShouldBeParsedTo0() {
    assertThat(underTest.execute("00:00", null)).isEqualTo(0);
  }

  @ParameterizedTest
  @CsvSource({"00:30,30", "12:30,750", "23:30,1410", "23:58,1438"})
  void executeShouldReturnHaveManyHalfMinutePassedSinceMidnight(String input, int expected) {
    assertThat(underTest.execute(input, null)).isEqualTo(expected);
  }
}
