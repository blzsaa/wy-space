package hu.blzsaa.wyspace.calculator;

import static org.assertj.core.api.Assertions.assertThat;

import hu.blzsaa.wyspace.PassDtoBuilder;
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
    underTest.addRange(new PassDtoBuilder().startTime(60).endTime(90).strength(1).build());

    // when
    var actual = underTest.findRange();

    // when
    assertThat(actual).isEqualTo(createFrom(60, sameStrengthEverywhere(1L)));
  }

  @Test
  void findRangeShouldReturnTheRangeOfTheStrongestSatelliteIfThereIsNoOverlappingRanges() {
    // given
    underTest.addRange(new PassDtoBuilder().startTime(60).endTime(90).strength(1).build());
    underTest.addRange(new PassDtoBuilder().startTime(90).endTime(150).strength(2).build());
    underTest.addRange(new PassDtoBuilder().startTime(180).endTime(210).strength(1).build());

    // when
    var actual = underTest.findRange();

    // when
    assertThat(actual).isEqualTo(createFrom(90, sameStrengthEverywhere(2L)));
  }

  @Test
  void findRangeShouldReturnTheRangeOfMaximumTotalDownlinkEvenWhenThereAreOverlappingRanges() {
    // given
    underTest.addRange(new PassDtoBuilder().startTime(60).endTime(90).strength(1).build());
    underTest.addRange(new PassDtoBuilder().startTime(90).endTime(150).strength(2).build());
    underTest.addRange(new PassDtoBuilder().startTime(180).endTime(210).strength(1).build());
    underTest.addRange(new PassDtoBuilder().startTime(180).endTime(210).strength(1).build());
    underTest.addRange(new PassDtoBuilder().startTime(180).endTime(210).strength(1).build());

    // when
    var actual = underTest.findRange();

    // when
    assertThat(actual).isEqualTo(createFrom(180, sameStrengthEverywhere(3L)));
  }

  @Test
  void findRangeShouldAddStrengthToAllTimeSlotsBetweenStartAndEndTime() {
    // given
    underTest.addRange(new PassDtoBuilder().startTime(60).endTime(150).strength(2).build());
    underTest.addRange(new PassDtoBuilder().startTime(120).endTime(150).strength(2).build());
    underTest.addRange(new PassDtoBuilder().startTime(210).endTime(240).strength(3).build());

    // when
    var actual = underTest.findRange();

    // when
    assertThat(actual).isEqualTo(createFrom(120, sameStrengthEverywhere(4L)));
  }

  @Test
  void findRangeShouldHandleRangesThatAreNotDividableBy30() {
    // given
    underTest.addRange(new PassDtoBuilder().startTime(60).endTime(90).strength(1).build());
    underTest.addRange(new PassDtoBuilder().startTime(90).endTime(150).strength(2).build());
    underTest.addRange(new PassDtoBuilder().startTime(183).endTime(213).strength(1).build());
    underTest.addRange(new PassDtoBuilder().startTime(185).endTime(215).strength(1).build());
    underTest.addRange(new PassDtoBuilder().startTime(180).endTime(210).strength(1).build());

    // when
    var actual = underTest.findRange();

    // when
    assertThat(actual)
        .isEqualTo(
            createFrom(
                183,
                Stream.of(
                        sameStrengthInSubList(2, 2),
                        sameStrengthInSubList(3, 25),
                        sameStrengthInSubList(2, 3))
                    .flatMap(Collection::stream)
                    .collect(Collectors.toList())));
  }

  @Test
  void addRangeShouldHandleMidnightForEndTimeAsWell() {
    // given
    int lastHalfHour = 23 * 60 + 30;
    underTest.addRange(new PassDtoBuilder().startTime(0).endTime(0).strength(1).build());
    underTest.addRange(new PassDtoBuilder().startTime(lastHalfHour).endTime(0).strength(1).build());

    // when
    var actual = underTest.findRange();

    // when
    assertThat(actual).isEqualTo(createFrom(lastHalfHour, sameStrengthEverywhere(2L)));
  }

  private MaximumTotalDownlinkResultParam createFrom(
      int indexOfStartOfTheMaxPeriod, List<Long> range) {
    MaximumTotalDownlinkResultParam dto = new MaximumTotalDownlinkResultParam();
    dto.setIndexOfStartOfTheMaxPeriod(indexOfStartOfTheMaxPeriod);
    dto.setRange(range);
    return dto;
  }

  private static List<Long> sameStrengthEverywhere(long x) {
    return sameStrengthInSubList(x, 30);
  }

  private static List<Long> sameStrengthInSubList(long x, int subListSize) {
    return IntStream.range(0, subListSize).mapToObj(i -> x).collect(Collectors.toList());
  }
}
