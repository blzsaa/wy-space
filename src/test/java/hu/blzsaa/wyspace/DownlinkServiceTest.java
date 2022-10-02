package hu.blzsaa.wyspace;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

import hu.blzsaa.wyspace.dto.InputDto;
import hu.blzsaa.wyspace.dto.PassDto;
import hu.blzsaa.wyspace.dto.ResultDto;
import hu.blzsaa.wyspace.fileinterpreter.PassScheduleInterpreter;
import hu.blzsaa.wyspace.util.ReaderFactory;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class DownlinkServiceTest {

  @Mock private PassScheduleInterpreter passScheduleInterpreter;
  @Mock private ReaderFactory readerFactory;
  @Mock private MaximumTotalDownlinkCalculator calculator;
  @Mock private DownloadPredicator downloadPredicator;
  @Mock private ResultTransformer resultTransformer;
  @Mock private InputStream inputStream;
  @Mock private BufferedReader bufferedReader;
  private DownlinkService underTest;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);

    underTest =
        new DownlinkService(
            passScheduleInterpreter,
            readerFactory,
            calculator,
            downloadPredicator,
            resultTransformer);
  }

  @Test
  void doTaskShouldAddRangesOneByOneToTHeCalculatorThenFindTheMaxAndCallDownloadDecider()
      throws IOException {
    // given
    InputDto inputDto = new InputDto(inputStream, 123L);
    doReturn(bufferedReader).when(readerFactory).createNewBufferedFileReader(inputStream);
    var passDto1 = new PassDto(1, 0, 0);
    var passDto2 = new PassDto(2, 0, 0);
    var passDto3 = new PassDto(3, 0, 0);
    doReturn(Stream.of(passDto1, passDto2, passDto3))
        .when(passScheduleInterpreter)
        .readInput(bufferedReader);
    doReturn(new MaximumTotalDownlinkResultParam(List.of(1L, 2L, 3L), 2))
        .when(calculator)
        .findRange();
    doReturn(true).when(downloadPredicator).isDownloadable(List.of(1L, 2L, 3L), 123L);
    doReturn(new ResultDto(LocalTime.NOON, LocalTime.NOON, true))
        .when(resultTransformer)
        .transform(new MaximumTotalDownlinkResultParam(List.of(1L, 2L, 3L), 2), true);

    // when
    var actual = underTest.doTask(inputDto);

    // then
    assertThat(actual).isEqualTo(new ResultDto(LocalTime.NOON, LocalTime.NOON, true));
    verify(calculator).addRange(passDto1);
    verify(calculator).addRange(passDto2);
    verify(calculator).addRange(passDto3);
  }
}
