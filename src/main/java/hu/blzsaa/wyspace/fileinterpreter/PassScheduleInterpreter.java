package hu.blzsaa.wyspace.fileinterpreter;

import hu.blzsaa.wyspace.dto.PassDto;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.List;
import org.supercsv.cellprocessor.ParseInt;
import org.supercsv.cellprocessor.constraint.NotNull;
import org.supercsv.cellprocessor.constraint.StrRegEx;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvBeanReader;
import org.supercsv.io.ICsvBeanReader;
import org.supercsv.prefs.CsvPreference;

public class PassScheduleInterpreter {

  private static final String[] HEADER = new String[] {"name", "strength", "startTime", "endTime"};
  private static final CellProcessor[] CELL_PROCESSORS =
      new CellProcessor[] {
        new NotNull(), // name
        new NotNull(new ParseInt()), // strength
        new NotNull(new StrRegEx("\\d\\d:\\d\\d", new ParseRange())), // startTime
        new NotNull(new StrRegEx("\\d\\d:\\d\\d", new ParseRange())), // endTime
      };

  public List<PassDto> readInput(BufferedReader br) {
    List<PassDto> passDtos = new ArrayList<>();
    try (ICsvBeanReader beanReader = new CsvBeanReader(br, CsvPreference.STANDARD_PREFERENCE)) {
      PassDto customer;
      while ((customer = beanReader.read(PassDto.class, HEADER, CELL_PROCESSORS)) != null) {
        passDtos.add(customer);
      }
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
    return passDtos;
  }
}
