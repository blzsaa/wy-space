package hu.blzsaa.wyspace;

import hu.blzsaa.wyspace.calculator.MaximumTotalDownlinkCalculator;
import hu.blzsaa.wyspace.fileinterpreter.PassScheduleInterpreter;
import hu.blzsaa.wyspace.util.ReaderFactory;
import java.io.BufferedReader;
import java.io.IOException;

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
    try (BufferedReader br = readerFactory.createNewBufferedFileReader(args[0])) {
      passScheduleInterpreter.readInput(br).forEach(calculator::addRange);
      return calculator.findRange();
    }
  }
}
