package hu.blzsaa.wyspace;

import static org.assertj.core.api.Assertions.assertThat;

import hu.blzsaa.wyspace.dto.ResultDto;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ResultTransformerTest {

  private ResultTransformer underTest;

  @BeforeEach
  void setUp() {
    underTest = new ResultTransformer();
  }

  @Test
  void transformShouldParseMinutesSinceMidnightToLocalTimeForStartTimeAddHalfHourForEndTime() {
    // given
    MaximumTotalDownlinkResultParam maximumTotalDownlinkResultParam =
        new MaximumTotalDownlinkResultParam(List.of(1L, 2L, 3L), 11 * 60 + 12);

    // when
    var actual = underTest.transform(maximumTotalDownlinkResultParam, true);

    // then
    ResultDto resultDto = new ResultDto(LocalTime.of(11, 12), LocalTime.of(11, 42), true);
    assertThat(actual).isEqualTo(resultDto);
  }
}
