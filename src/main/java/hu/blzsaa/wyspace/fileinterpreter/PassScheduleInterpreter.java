package hu.blzsaa.wyspace.fileinterpreter;

import hu.blzsaa.wyspace.dto.PassDto;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class PassScheduleInterpreter {

  private final LineParser lineParser;

  public PassScheduleInterpreter(LineParser lineParser) {
    this.lineParser = lineParser;
  }

  public Stream<PassDto> readInput(BufferedReader br) {
    return IntStream.iterate(1, i -> i + 1)
        .mapToObj(i -> new Pair(i, readLineWith(br)))
        .takeWhile(i -> i.line() != null)
        .map(i -> new FileLineDto(i.lineNumber, List.of(i.line.split(","))))
        .map(lineParser::parseLine);
  }

  private static String readLineWith(BufferedReader br) {
    try {
      return br.readLine();
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  record Pair(int lineNumber, String line) {}
}
