package hu.blzsaa.wyspace;

import hu.blzsaa.wyspace.dto.InputDto;
import hu.blzsaa.wyspace.dto.ResultDto;
import hu.blzsaa.wyspace.fileinterpreter.PassScheduleInterpreter;
import hu.blzsaa.wyspace.util.ReaderFactory;
import java.io.BufferedReader;
import java.io.IOException;
import javax.enterprise.context.ApplicationScoped;
import javax.validation.Valid;

@ApplicationScoped
public class DownlinkService {

  private final PassScheduleInterpreter passScheduleInterpreter;
  private final ReaderFactory readerFactory;
  private final MaximumTotalDownlinkCalculator calculator;
  private final DownloadPredicator downloadPredicator;
  private final ResultTransformer resultTransformer;

  public DownlinkService(
      PassScheduleInterpreter passScheduleInterpreter,
      ReaderFactory readerFactory,
      MaximumTotalDownlinkCalculator calculator,
      DownloadPredicator downloadPredicator,
      ResultTransformer resultTransformer) {
    this.passScheduleInterpreter = passScheduleInterpreter;
    this.readerFactory = readerFactory;
    this.calculator = calculator;
    this.downloadPredicator = downloadPredicator;
    this.resultTransformer = resultTransformer;
  }

  public ResultDto doTask(@Valid InputDto inputDto) throws IOException {
    try (BufferedReader br = readerFactory.createNewBufferedFileReader(inputDto.getInputStream())) {
      passScheduleInterpreter.readInput(br).forEach(calculator::addRange);
      MaximumTotalDownlinkResultParam dto = calculator.findRange();
      boolean downloadable =
          downloadPredicator.isDownloadable(dto.getRange(), inputDto.getBandwidth());
      return resultTransformer.transform(dto, downloadable);
    }
  }
}
