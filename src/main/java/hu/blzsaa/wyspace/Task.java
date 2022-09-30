package hu.blzsaa.wyspace;

import hu.blzsaa.wyspace.calculator.TaskHandler;
import hu.blzsaa.wyspace.dto.ResultDto;
import hu.blzsaa.wyspace.fileinterpreter.PassScheduleInterpreter;
import hu.blzsaa.wyspace.util.ReaderFactory;
import java.io.BufferedReader;
import java.io.IOException;

public class Task {

  private final PassScheduleInterpreter passScheduleInterpreter;
  private final TaskHandler taskHandler;
  private final ReaderFactory readerFactory;

  public Task(
      PassScheduleInterpreter passScheduleInterpreter,
      TaskHandler taskHandler,
      ReaderFactory readerFactory) {
    this.passScheduleInterpreter = passScheduleInterpreter;
    this.readerFactory = readerFactory;
    this.taskHandler = taskHandler;
  }

  public ResultDto doTask(String[] args) throws IOException {
    try (BufferedReader br = readerFactory.createNewBufferedFileReader(args[0])) {
      passScheduleInterpreter.readInput(br).forEach(taskHandler::addRange);
      return taskHandler.findMaxRangeAndDecideIfItCanBeDownloadedOrNot(Long.parseLong(args[1]));
    }
  }
}
