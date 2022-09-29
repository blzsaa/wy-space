package hu.blzsaa.wyspace.calculator;

import hu.blzsaa.wyspace.dto.PassDto;
import hu.blzsaa.wyspace.exception.NoRangeFoundException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.IntStream;

public class MaximumTotalDownlinkCalculator {
  public int findRange(List<PassDto> passDtos) {
    if (passDtos == null || passDtos.isEmpty()) throw new NoRangeFoundException();
    Long[] rangeStrengths = new Long[48];
    Arrays.fill(rangeStrengths, 0L);

    passDtos.forEach(
        p ->
            IntStream.range(p.getStartTime(), p.getEndTime())
                .forEach(i -> rangeStrengths[i] += p.getStrength()));

    return IntStream.range(0, rangeStrengths.length)
        .mapToObj(i -> new IndexRangeStrengthPair(i, rangeStrengths[i]))
        .max(Comparator.comparingLong(IndexRangeStrengthPair::getRangeStrength))
        .orElseThrow(NoRangeFoundException::new)
        .getIndex();
  }
}
