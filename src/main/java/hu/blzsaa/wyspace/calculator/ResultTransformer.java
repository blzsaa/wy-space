package hu.blzsaa.wyspace.calculator;

import hu.blzsaa.wyspace.dto.ResultDto;
import java.time.LocalTime;

public class ResultTransformer {

  public ResultDto transform(MaximumTotalDownlinkResultParam dto, boolean downloadable) {
    ResultDto resultDto = new ResultDto();
    resultDto.setDownloadable(downloadable);
    resultDto.setStartTime(createLocalTimeFromMinute(dto.getIndexOfStartOfTheMaxPeriod()));
    resultDto.setEndTime(createLocalTimeFromMinute(dto.getIndexOfStartOfTheMaxPeriod() + 30));
    return resultDto;
  }

  private LocalTime createLocalTimeFromMinute(int minutesSinceMidnight) {
    return LocalTime.of(minutesSinceMidnight / 60, minutesSinceMidnight % 60);
  }
}
