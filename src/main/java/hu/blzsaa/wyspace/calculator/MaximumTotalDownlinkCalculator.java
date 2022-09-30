package hu.blzsaa.wyspace.calculator;

import hu.blzsaa.wyspace.dto.PassDto;
import hu.blzsaa.wyspace.exception.NoRangeFoundException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class MaximumTotalDownlinkCalculator {

  private final List<Long> rangeStrengths;

  public MaximumTotalDownlinkCalculator() {
    rangeStrengths = IntStream.range(0, 24 * 60).mapToObj(i -> 0L).collect(Collectors.toList());
  }

  public void addRange(PassDto p) {
    int endTime = p.getEndTime() == 0 ? 24 * 60 : p.getEndTime();
    IntStream.range(p.getStartTime(), endTime)
        .forEach(i -> rangeStrengths.set(i, rangeStrengths.get(i) + p.getStrength()));
  }

  public MaximumTotalDownlinkResultParam findRange() {
    return IntStream.range(0, rangeStrengths.size() - 29)
        .parallel()
        .mapToObj(this::calculateTotalDownlinkOfPeriodStartingAtIndex)
        .max(Comparator.comparing(TotalStrengthAtPeriod::getStrength))
        .map(this::transform)
        .map(this::validateResult)
        .orElseThrow(NoRangeFoundException::new);
  }

  private MaximumTotalDownlinkResultParam validateResult(MaximumTotalDownlinkResultParam a) {
    if (rangeStrengths.get(a.getIndexOfStartOfTheMaxPeriod()) == 0) {
      throw new NoRangeFoundException();
    }
    return a;
  }

  private MaximumTotalDownlinkResultParam transform(TotalStrengthAtPeriod max) {
    MaximumTotalDownlinkResultParam dto = new MaximumTotalDownlinkResultParam();
    dto.setIndexOfStartOfTheMaxPeriod(max.getStartingIndexOfPeriod());
    dto.setRange(
        rangeStrengths.subList(
            max.getStartingIndexOfPeriod(), max.getStartingIndexOfPeriod() + 30));
    return dto;
  }

  private TotalStrengthAtPeriod calculateTotalDownlinkOfPeriodStartingAtIndex(int index) {
    TotalStrengthAtPeriod param = new TotalStrengthAtPeriod();
    param.setStartingIndexOfPeriod(index);
    param.setStrength(IntStream.range(index, index + 30).mapToLong(rangeStrengths::get).sum());
    return param;
  }
}
