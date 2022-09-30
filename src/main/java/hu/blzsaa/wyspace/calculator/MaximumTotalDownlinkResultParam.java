package hu.blzsaa.wyspace.calculator;

import java.util.List;
import java.util.Objects;

public class MaximumTotalDownlinkResultParam {
  private List<Long> range;

  private int indexOfStartOfTheMaxPeriod;

  public List<Long> getRange() {
    return range;
  }

  public void setRange(List<Long> range) {
    this.range = range;
  }

  public int getIndexOfStartOfTheMaxPeriod() {
    return indexOfStartOfTheMaxPeriod;
  }

  public void setIndexOfStartOfTheMaxPeriod(int indexOfStartOfTheMaxPeriod) {
    this.indexOfStartOfTheMaxPeriod = indexOfStartOfTheMaxPeriod;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    MaximumTotalDownlinkResultParam dto = (MaximumTotalDownlinkResultParam) o;

    if (indexOfStartOfTheMaxPeriod != dto.indexOfStartOfTheMaxPeriod) return false;
    return Objects.equals(range, dto.range);
  }

  @Override
  public int hashCode() {
    int result = range != null ? range.hashCode() : 0;
    result = 31 * result + indexOfStartOfTheMaxPeriod;
    return result;
  }

  @Override
  public String toString() {
    return "MaximumTotalDownlinkResultParam{"
        + "range="
        + range
        + ", indexOfStartOfTheMaxPeriod="
        + indexOfStartOfTheMaxPeriod
        + '}';
  }
}
