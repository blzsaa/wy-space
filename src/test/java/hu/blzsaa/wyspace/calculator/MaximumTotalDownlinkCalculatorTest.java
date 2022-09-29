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
    underTest.addRange(new PassDtoBuilder().startTime(2).endTime(3).strength(1).build());

    // when
    var actual = underTest.findRange();

    // when
    assertThat(actual).isEqualTo(2);
  }

  @Test
  void findRangeShouldReturnTheRangeOfTheStrongestSatelliteIfThereIsNoOverlappingRanges() {
    // given
    underTest.addRange(new PassDtoBuilder().startTime(2).endTime(3).strength(1).build());
    underTest.addRange(new PassDtoBuilder().startTime(3).endTime(5).strength(2).build());
    underTest.addRange(new PassDtoBuilder().startTime(6).endTime(7).strength(1).build());

    // when
    var actual = underTest.findRange();

    // when
    assertThat(actual).isEqualTo(3);
  }

  @Test
  void findRangeShouldReturnTheRangeOfMaximumTotalDownlinkEvenWhenThereAreOverlappingRanges() {
    // given
    underTest.addRange(new PassDtoBuilder().startTime(2).endTime(3).strength(1).build());
    underTest.addRange(new PassDtoBuilder().startTime(3).endTime(5).strength(2).build());
    underTest.addRange(new PassDtoBuilder().startTime(6).endTime(7).strength(1).build());
    underTest.addRange(new PassDtoBuilder().startTime(6).endTime(7).strength(1).build());
    underTest.addRange(new PassDtoBuilder().startTime(6).endTime(7).strength(1).build());

    // when
    var actual = underTest.findRange();

    // when
    assertThat(actual).isEqualTo(6);
  }

  @Test
  void findRangeShouldAddStrengthToAllTimeSlotsBetweenStartAndEndTime() {
    // given
    underTest.addRange(new PassDtoBuilder().startTime(2).endTime(5).strength(2).build());
    underTest.addRange(new PassDtoBuilder().startTime(4).endTime(5).strength(2).build());
    underTest.addRange(new PassDtoBuilder().startTime(7).endTime(8).strength(3).build());

    // when
    var actual = underTest.findRange();

    // when
    assertThat(actual).isEqualTo(4);
  }
}
