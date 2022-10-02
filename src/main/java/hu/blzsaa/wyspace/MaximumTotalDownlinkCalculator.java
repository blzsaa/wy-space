package hu.blzsaa.wyspace;

import hu.blzsaa.wyspace.dto.PassDto;
import hu.blzsaa.wyspace.exception.NoRangeFoundException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
class MaximumTotalDownlinkCalculator {

  private final List<Long> rangeStrengths;

  public MaximumTotalDownlinkCalculator() {
    rangeStrengths = IntStream.range(0, 24 * 60).mapToObj(i -> 0L).collect(Collectors.toList());
  }

  public void addRange(PassDto p) {
    int endTime = p.endTime() == 0 ? 24 * 60 : p.endTime();
    IntStream.range(p.startTime(), endTime)
        .forEach(i -> rangeStrengths.set(i, rangeStrengths.get(i) + p.strength()));
  }

  public MaximumTotalDownlinkResultParam findRange() {
    return IntStream.range(0, rangeStrengths.size() - 29)
        .parallel()
        .mapToObj(this::calculateTotalDownlinkOfPeriodStartingAtIndex)
        .max(Comparator.comparing(TotalStrengthAtPeriod::strength))
        .map(this::transform)
        .map(this::validateResult)
        .orElseThrow(NoRangeFoundException::new);
  }

  private MaximumTotalDownlinkResultParam validateResult(MaximumTotalDownlinkResultParam a) {
    if (rangeStrengths.get(a.indexOfStartOfTheMaxPeriod()) == 0) {
      throw new NoRangeFoundException();
    }
    return a;
  }

  private MaximumTotalDownlinkResultParam transform(TotalStrengthAtPeriod max) {
    List<Long> range =
        rangeStrengths.subList(max.startingIndexOfPeriod(), max.startingIndexOfPeriod() + 30);
    return new MaximumTotalDownlinkResultParam(range, max.startingIndexOfPeriod());
  }

  private TotalStrengthAtPeriod calculateTotalDownlinkOfPeriodStartingAtIndex(int index) {
    return new TotalStrengthAtPeriod(
        index, IntStream.range(index, index + 30).mapToLong(rangeStrengths::get).sum());
  }
}
