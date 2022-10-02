package hu.blzsaa.wyspace;

import static org.assertj.core.api.Assertions.assertThat;

import hu.blzsaa.wyspace.dto.PassDto;
import hu.blzsaa.wyspace.exception.NoRangeFoundException;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MaximumTotalDownlinkCalculatorTest {
  private MaximumTotalDownlinkCalculator underTest;

  @BeforeEach
  void setUp() {
    underTest = new MaximumTotalDownlinkCalculator();
  }

  @Test
  void findRangeShouldThrowNoRangeFoundExceptionWhenNoRangeWereAdded() {
    // when
    Throwable actual = Assertions.catchThrowable(() -> underTest.findRange());

    // then
    assertThat(actual).isInstanceOf(NoRangeFoundException.class);
  }

  @Test
  void findRangeShouldReturnTheRangeOfTheSatelliteIfThereIsOnlyOnePresent() {
    // given
    underTest.addRange(new PassDto(1, 60, 90));

    // when
    var actual = underTest.findRange();

    // when
    assertThat(actual)
        .isEqualTo(new MaximumTotalDownlinkResultParam(sameStrengthEverywhere(1L), 60));
  }

  @Test
  void findRangeShouldReturnTheRangeOfTheStrongestSatelliteIfThereIsNoOverlappingRanges() {
    // given
    underTest.addRange(new PassDto(1, 60, 90));
    underTest.addRange(new PassDto(2, 90, 150));
    underTest.addRange(new PassDto(1, 180, 210));

    // when
    var actual = underTest.findRange();

    // when
    assertThat(actual)
        .isEqualTo(new MaximumTotalDownlinkResultParam(sameStrengthEverywhere(2L), 90));
  }

  @Test
  void findRangeShouldReturnTheRangeOfMaximumTotalDownlinkEvenWhenThereAreOverlappingRanges() {
    // given
    underTest.addRange(new PassDto(1, 60, 90));
    underTest.addRange(new PassDto(2, 90, 150));
    underTest.addRange(new PassDto(1, 180, 210));
    underTest.addRange(new PassDto(1, 180, 210));
    underTest.addRange(new PassDto(1, 180, 210));

    // when
    var actual = underTest.findRange();

    // when
    assertThat(actual)
        .isEqualTo(new MaximumTotalDownlinkResultParam(sameStrengthEverywhere(3L), 180));
  }

  @Test
  void findRangeShouldAddStrengthToAllTimeSlotsBetweenStartAndEndTime() {
    // given
    underTest.addRange(new PassDto(2, 60, 150));
    underTest.addRange(new PassDto(2, 120, 150));
    underTest.addRange(new PassDto(3, 210, 240));

    // when
    var actual = underTest.findRange();

    // when
    assertThat(actual)
        .isEqualTo(new MaximumTotalDownlinkResultParam(sameStrengthEverywhere(4L), 120));
  }

  @Test
  void findRangeShouldHandleRangesThatAreNotDividableBy30() {
    // given
    underTest.addRange(new PassDto(1, 60, 90));
    underTest.addRange(new PassDto(2, 90, 150));
    underTest.addRange(new PassDto(1, 183, 213));
    underTest.addRange(new PassDto(1, 185, 215));
    underTest.addRange(new PassDto(1, 180, 210));

    // when
    var actual = underTest.findRange();

    // when
    assertThat(actual)
        .isEqualTo(
            new MaximumTotalDownlinkResultParam(
                Stream.of(
                        sameStrengthInSubList(2, 2),
                        sameStrengthInSubList(3, 25),
                        sameStrengthInSubList(2, 3))
                    .flatMap(Collection::stream)
                    .collect(Collectors.toList()),
                183));
  }

  @Test
  void addRangeShouldHandleMidnightForEndTimeAsWell() {
    // given
    int lastHalfHour = 23 * 60 + 30;
    underTest.addRange(new PassDto(1, 0, 0));
    underTest.addRange(new PassDto(1, lastHalfHour, 0));

    // when
    var actual = underTest.findRange();

    // when
    assertThat(actual)
        .isEqualTo(new MaximumTotalDownlinkResultParam(sameStrengthEverywhere(2L), lastHalfHour));
  }

  private static List<Long> sameStrengthEverywhere(long x) {
    return sameStrengthInSubList(x, 30);
  }

  private static List<Long> sameStrengthInSubList(long x, int subListSize) {
    return IntStream.range(0, subListSize).mapToObj(i -> x).collect(Collectors.toList());
  }
}
