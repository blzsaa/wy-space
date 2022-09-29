package hu.blzsaa.wyspace.calculator;

import static org.assertj.core.api.Assertions.assertThat;

import hu.blzsaa.wyspace.PassDtoBuilder;
import hu.blzsaa.wyspace.dto.PassDto;
import hu.blzsaa.wyspace.exception.NoRangeFoundException;
import java.util.List;
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
  void findRangeShouldThrowNoRangeFoundExceptionWhenInputIsEmpty() {
    // when
    Throwable actual = Assertions.catchThrowable(() -> underTest.findRange(List.of()));

    // then
    assertThat(actual).isInstanceOf(NoRangeFoundException.class);
  }

  @Test
  void findRangeShouldThrowNoRangeFoundExceptionWhenInputIsNull() {
    // when
    Throwable actual = Assertions.catchThrowable(() -> underTest.findRange(null));

    // then
    assertThat(actual).isInstanceOf(NoRangeFoundException.class);
  }

  @Test
  void findRangeShouldReturnTheRangeOfTheSatelliteIfThereIsOnlyOnePresent() {
    // given
    List<PassDto> passDtos =
        List.of(new PassDtoBuilder().startTime(2).endTime(3).strength(1).build());

    // when
    var actual = underTest.findRange(passDtos);

    // when
    assertThat(actual).isEqualTo(2);
  }

  @Test
  void findRangeShouldReturnTheRangeOfTheStrongestSatelliteIfThereIsNoOverlappingRanges() {
    // given
    List<PassDto> passDtos =
        List.of(
            new PassDtoBuilder().startTime(2).endTime(3).strength(1).build(),
            new PassDtoBuilder().startTime(3).endTime(5).strength(2).build(),
            new PassDtoBuilder().startTime(6).endTime(7).strength(1).build());

    // when
    var actual = underTest.findRange(passDtos);

    // when
    assertThat(actual).isEqualTo(3);
  }

  @Test
  void findRangeShouldReturnTheRangeOfMaximumTotalDownlinkEvenWhenThereAreOverlappingRanges() {
    // given
    List<PassDto> passDtos =
        List.of(
            new PassDtoBuilder().startTime(2).endTime(3).strength(1).build(),
            new PassDtoBuilder().startTime(3).endTime(5).strength(2).build(),
            new PassDtoBuilder().startTime(6).endTime(7).strength(1).build(),
            new PassDtoBuilder().startTime(6).endTime(7).strength(1).build(),
            new PassDtoBuilder().startTime(6).endTime(7).strength(1).build());

    // when
    var actual = underTest.findRange(passDtos);

    // when
    assertThat(actual).isEqualTo(6);
  }

  @Test
  void findRangeShouldAddStrengthToAllTimeSlotsBetweenStartAndEndTime() {
    // given
    List<PassDto> passDtos =
        List.of(
            new PassDtoBuilder().startTime(2).endTime(5).strength(2).build(),
            new PassDtoBuilder().startTime(4).endTime(5).strength(2).build(),
            new PassDtoBuilder().startTime(7).endTime(8).strength(3).build());

    // when
    var actual = underTest.findRange(passDtos);

    // when
    assertThat(actual).isEqualTo(4);
  }
}
