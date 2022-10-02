package hu.blzsaa.wyspace;

import hu.blzsaa.wyspace.dto.ResultDto;
import java.time.LocalTime;
import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
class ResultTransformer {

  public ResultDto transform(MaximumTotalDownlinkResultParam dto, boolean downloadable) {
    ResultDto resultDto =
        new ResultDto(
            createLocalTimeFromMinute(dto.indexOfStartOfTheMaxPeriod()),
            createLocalTimeFromMinute(dto.indexOfStartOfTheMaxPeriod() + 30),
            downloadable);
    return resultDto;
  }

  private LocalTime createLocalTimeFromMinute(int minutesSinceMidnight) {
    return LocalTime.of(minutesSinceMidnight / 60, minutesSinceMidnight % 60);
  }
}
