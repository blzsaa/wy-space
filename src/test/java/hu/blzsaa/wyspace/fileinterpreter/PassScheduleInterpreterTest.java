package hu.blzsaa.wyspace.fileinterpreter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;

import hu.blzsaa.wyspace.dto.PassDto;
import java.io.BufferedReader;
import java.io.StringReader;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class PassScheduleInterpreterTest {
  @Mock LineParser lineParser;

  private PassScheduleInterpreter underTest;
  private AutoCloseable openMocks;

  @BeforeEach
  void setUp() {
    openMocks = MockitoAnnotations.openMocks(this);
    underTest = new PassScheduleInterpreter(lineParser);
  }

  @AfterEach
  void tearDown() throws Exception {
    openMocks.close();
  }

  @Test
  void readInputShouldParseIntoParseRangeDtoAndCallLineParser() {
    // given
    BufferedReader reader = new BufferedReader(new StringReader("1\n2\n3"));
    doReturn(new PassDto(1, 0, 0)).when(lineParser).parseLine(new FileLineDto(1, List.of("1")));
    doReturn(new PassDto(2, 0, 0)).when(lineParser).parseLine(new FileLineDto(2, List.of("2")));
    doReturn(new PassDto(3, 0, 0)).when(lineParser).parseLine(new FileLineDto(3, List.of("3")));

    // when
    var actual = underTest.readInput(reader);

    // then
    assertThat(actual)
        .containsOnly(new PassDto(1, 0, 0), new PassDto(2, 0, 0), new PassDto(3, 0, 0));
  }
}
