package hu.blzsaa.wyspace.calculator;

import java.util.List;

public class DownloadPredicator {
  public boolean isDownloadable(List<Long> range, long maxDownloadSpeed) {
    return range.stream().allMatch(delta -> delta <= maxDownloadSpeed);
  }
}
