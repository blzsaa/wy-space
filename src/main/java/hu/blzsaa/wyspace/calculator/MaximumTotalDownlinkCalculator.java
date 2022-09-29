package hu.blzsaa.wyspace.calculator;

import hu.blzsaa.wyspace.dto.PassDto;
import hu.blzsaa.wyspace.exception.NoRangeFoundException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.stream.IntStream;

public class MaximumTotalDownlinkCalculator {

  private final Long[] rangeStrengths;

  public MaximumTotalDownlinkCalculator() {
    rangeStrengths = new Long[48];
    Arrays.fill(rangeStrengths, 0L);
  }

  public void addRange(PassDto p) {
    IntStream.range(p.getStartTime(), p.getEndTime())
        .forEach(i -> rangeStrengths[i] += p.getStrength());
  }

  public int findRange() {
    int index =
        IntStream.range(0, rangeStrengths.length)
            .mapToObj(i -> new IndexRangeStrengthPair(i, rangeStrengths[i]))
            .max(Comparator.comparingLong(IndexRangeStrengthPair::getRangeStrength))
            .orElseThrow(NoRangeFoundException::new)
            .getIndex();
    if (rangeStrengths[index] == 0) {
      throw new NoRangeFoundException();
    }
    return index;
  }
}
