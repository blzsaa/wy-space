package hu.blzsaa.wyspace.fileinterpreter;

import static org.assertj.core.api.Assertions.assertThat;

import hu.blzsaa.wyspace.dto.PassDto;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LineParserTest {
  private LineParser underTest;

  @BeforeEach
  void setUp() {
    underTest = new LineParser();
  }

  @Test
  void parseLineShouldParseEmptyLineToEmptyList() {
    // given
    FileLineDto fileLineDto = new FileLineDto(1, List.of("Red,D,w,arf,", "2", "12:51", "13:30"));

    // when
    var actual = underTest.parseLine(fileLineDto);

    // then
    assertThat(actual).isEqualTo(new PassDto(2, 771, 810));
  }
}
