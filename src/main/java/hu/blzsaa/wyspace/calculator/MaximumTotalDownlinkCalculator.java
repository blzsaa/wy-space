package hu.blzsaa.wyspace.calculator;

import hu.blzsaa.wyspace.dto.PassDto;
import hu.blzsaa.wyspace.exception.NoRangeFoundException;
import java.util.Arrays;
import java.util.stream.IntStream;

public class MaximumTotalDownlinkCalculator {

  private final Long[] rangeStrengths;

  public MaximumTotalDownlinkCalculator() {
    rangeStrengths = new Long[24 * 60];
    Arrays.fill(rangeStrengths, 0L);
  }

  public void addRange(PassDto p) {
    IntStream.range(p.getStartTime(), p.getEndTime())
        .forEach(i -> rangeStrengths[i] += p.getStrength());
  }

  public int findRange() {
    long strengthOfFirstRange = Arrays.stream(rangeStrengths, 0, 30).mapToLong(i -> i).sum();
    long startOfTheMaxPeriod = strengthOfFirstRange;
    long period = strengthOfFirstRange;
    int indexOfStartOfTheMaxPeriod = 0;
    for (int i = 1; i < rangeStrengths.length - 30; i++) {
      period = period - rangeStrengths[i - 1] + rangeStrengths[i + 29];
      if (period > startOfTheMaxPeriod) {
        indexOfStartOfTheMaxPeriod = i;
        startOfTheMaxPeriod = period;
      }
    }
    if (rangeStrengths[indexOfStartOfTheMaxPeriod] == 0) {
      throw new NoRangeFoundException();
    }
    return indexOfStartOfTheMaxPeriod;
  }
}
