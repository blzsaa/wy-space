package hu.blzsaa.wyspace;

import java.util.List;
import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
class DownloadPredicator {
  public boolean isDownloadable(List<Long> range, long maxDownloadSpeed) {
    return range.stream().allMatch(delta -> delta <= maxDownloadSpeed);
  }
}
