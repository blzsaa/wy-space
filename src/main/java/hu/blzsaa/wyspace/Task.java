package hu.blzsaa.wyspace;

import hu.blzsaa.wyspace.calculator.MaximumTotalDownlinkCalculator;
import hu.blzsaa.wyspace.dto.PassDto;
import hu.blzsaa.wyspace.fileinterpreter.PassScheduleInterpreter;
import hu.blzsaa.wyspace.util.ReaderFactory;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

public class Task {

  private final PassScheduleInterpreter passScheduleInterpreter;
  private final MaximumTotalDownlinkCalculator calculator;
  private final ReaderFactory readerFactory;

  public Task(
      PassScheduleInterpreter passScheduleInterpreter,
      MaximumTotalDownlinkCalculator calculator,
      ReaderFactory readerFactory) {
    this.passScheduleInterpreter = passScheduleInterpreter;
    this.calculator = calculator;
    this.readerFactory = readerFactory;
  }

  public int doTask(String[] args) throws IOException {
    BufferedReader br = readerFactory.createNewBufferedFileReader(args[0]);
    List<PassDto> passDtos = passScheduleInterpreter.readInput(br);
    return calculator.findRange(passDtos);
  }
}
