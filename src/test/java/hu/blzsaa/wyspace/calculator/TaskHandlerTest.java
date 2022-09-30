package hu.blzsaa.wyspace.calculator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

import hu.blzsaa.wyspace.PassDtoBuilder;
import hu.blzsaa.wyspace.dto.PassDto;
import hu.blzsaa.wyspace.dto.ResultDto;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class TaskHandlerTest {

  @Mock private MaximumTotalDownlinkCalculator calculator;
  @Mock private DownloadPredicator downloadPredicator;
  @Mock private ResultTransformer resultTransformer;

  private TaskHandler underTest;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    underTest = new TaskHandler(calculator, downloadPredicator, resultTransformer);
  }

  @Test
  void addRangeShouldDelegateToCalculator() {
    // given
    PassDto p = new PassDtoBuilder().name("name").startTime(11).endTime(12).build();

    // when
    underTest.addRange(p);

    // then
    verify(calculator).addRange(p);
  }

  @Test
  void findMaxRangeAndDecideIfItCanBeDownloadedOrNotShouldDelegateToCalculator() {
    // given
    MaximumTotalDownlinkResultParam maximumTotalDownlinkResultParam =
        new MaximumTotalDownlinkResultParam();
    List<Long> range = List.of(1L, 2L, 3L);
    maximumTotalDownlinkResultParam.setRange(range);
    maximumTotalDownlinkResultParam.setIndexOfStartOfTheMaxPeriod(2);

    ResultDto resultDto = new ResultDto();
    resultDto.setStartTime(LocalTime.of(11, 12));
    resultDto.setEndTime(LocalTime.of(11, 13));
    resultDto.setDownloadable(true);

    doReturn(maximumTotalDownlinkResultParam).when(calculator).findRange();
    doReturn(true).when(downloadPredicator).isDownloadable(range, 18L);
    doReturn(resultDto).when(resultTransformer).transform(maximumTotalDownlinkResultParam, true);

    // when
    var actual = underTest.findMaxRangeAndDecideIfItCanBeDownloadedOrNot(18L);

    // then
    assertThat(actual).isEqualTo(resultDto);
  }
}
