package hu.blzsaa.wyspace.calculator;

import hu.blzsaa.wyspace.dto.PassDto;
import hu.blzsaa.wyspace.dto.ResultDto;

public class TaskHandler {

  private final MaximumTotalDownlinkCalculator calculator;
  private final DownloadPredicator downloadPredicator;
  private final ResultTransformer resultTransformer;

  public TaskHandler(
      MaximumTotalDownlinkCalculator calculator,
      DownloadPredicator downloadPredicator,
      ResultTransformer resultTransformer) {
    this.calculator = calculator;
    this.downloadPredicator = downloadPredicator;
    this.resultTransformer = resultTransformer;
  }

  public void addRange(PassDto p) {
    calculator.addRange(p);
  }

  public ResultDto findMaxRangeAndDecideIfItCanBeDownloadedOrNot(long maxDownloadSpeed) {
    MaximumTotalDownlinkResultParam dto = calculator.findRange();
    boolean downloadable = downloadPredicator.isDownloadable(dto.getRange(), maxDownloadSpeed);
    return resultTransformer.transform(dto, downloadable);
  }
}
