package hu.blzsaa.wyspace.calculator;

import static org.assertj.core.api.Assertions.assertThat;

import hu.blzsaa.wyspace.PassDtoBuilder;
import hu.blzsaa.wyspace.exception.NoRangeFoundException;
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
    assertThat(actual).isEqualTo(2 * 30);
  }

  @Test
  void findRangeShouldReturnTheRangeOfTheStrongestSatelliteIfThereIsNoOverlappingRanges() {
    // given
    underTest.addRange(new PassDtoBuilder().startTime(2 * 30).endTime(3 * 30).strength(1).build());
    underTest.addRange(new PassDtoBuilder().startTime(3 * 30).endTime(5 * 30).strength(2).build());
    underTest.addRange(new PassDtoBuilder().startTime(6 * 30).endTime(7 * 30).strength(1).build());

    // when
    var actual = underTest.findRange();

    // when
    assertThat(actual).isEqualTo(3 * 30);
  }

  @Test
  void findRangeShouldReturnTheRangeOfMaximumTotalDownlinkEvenWhenThereAreOverlappingRanges() {
    // given
    underTest.addRange(new PassDtoBuilder().startTime(2 * 30).endTime(3 * 30).strength(1).build());
    underTest.addRange(new PassDtoBuilder().startTime(3 * 30).endTime(5 * 30).strength(2).build());
    underTest.addRange(new PassDtoBuilder().startTime(6 * 30).endTime(7 * 30).strength(1).build());
    underTest.addRange(new PassDtoBuilder().startTime(6 * 30).endTime(7 * 30).strength(1).build());
    underTest.addRange(new PassDtoBuilder().startTime(6 * 30).endTime(7 * 30).strength(1).build());

    // when
    var actual = underTest.findRange();

    // when
    assertThat(actual).isEqualTo(6 * 30);
  }

  @Test
  void findRangeShouldAddStrengthToAllTimeSlotsBetweenStartAndEndTime() {
    // given
    underTest.addRange(new PassDtoBuilder().startTime(2 * 30).endTime(5 * 30).strength(2).build());
    underTest.addRange(new PassDtoBuilder().startTime(4 * 30).endTime(5 * 30).strength(2).build());
    underTest.addRange(new PassDtoBuilder().startTime(7 * 30).endTime(8 * 30).strength(3).build());

    // when
    var actual = underTest.findRange();

    // when
    assertThat(actual).isEqualTo(4 * 30);
  }

  @Test
  void findRangeShouldHandleRangesThatAreNotDividableBy30() {
    // given
    underTest.addRange(new PassDtoBuilder().startTime(2 * 30).endTime(3 * 30).strength(1).build());
    underTest.addRange(new PassDtoBuilder().startTime(3 * 30).endTime(5 * 30).strength(2).build());
    underTest.addRange(
        new PassDtoBuilder().startTime(6 * 30 + 3).endTime(7 * 30 + 3).strength(1).build());
    underTest.addRange(
        new PassDtoBuilder().startTime(6 * 30 + 5).endTime(7 * 30 + 5).strength(1).build());
    underTest.addRange(new PassDtoBuilder().startTime(6 * 30).endTime(7 * 30).strength(1).build());

    // when
    var actual = underTest.findRange();

    // when
    assertThat(actual).isEqualTo(6 * 30 + 3);
  }
}
