package hu.blzsaa.wyspace.fileinterpreter;

import org.supercsv.cellprocessor.CellProcessorAdaptor;
import org.supercsv.cellprocessor.ift.StringCellProcessor;
import org.supercsv.util.CsvContext;

class ParseRange extends CellProcessorAdaptor implements StringCellProcessor {
  @Override
  // examples at page of SuperCsv are also using the same way
  @SuppressWarnings("unchecked")
  public Object execute(Object value, CsvContext context) {
    validateInputNotNull(value, context); // throws an Exception if the input is null

    String time = value.toString();
    var times = time.split(":");
    int hour = Integer.parseInt(times[0]);
    int minute = Integer.parseInt(times[1]);
    return hour * 60 + minute;
  }
}
