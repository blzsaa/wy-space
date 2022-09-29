package hu.blzsaa.wyspace;

import hu.blzsaa.wyspace.calculator.MaximumTotalDownlinkCalculator;
import hu.blzsaa.wyspace.fileinterpreter.PassScheduleInterpreter;
import hu.blzsaa.wyspace.util.ReaderFactory;
import java.io.IOException;

public class Main {
  public static void main(String[] args) throws IOException {
    Task task = dummyDI();

    System.out.println("calculator.calculate(passes) = " + task.doTask(args));
  }

  public static Task dummyDI() {
    PassScheduleInterpreter passScheduleInterpreter = new PassScheduleInterpreter();
    MaximumTotalDownlinkCalculator calculator = new MaximumTotalDownlinkCalculator();
    ReaderFactory readerFactory = new ReaderFactory();
    return new Task(passScheduleInterpreter, calculator, readerFactory);
  }
}
