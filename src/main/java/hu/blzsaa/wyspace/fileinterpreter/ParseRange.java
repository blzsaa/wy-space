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
    int hourInRange = Integer.parseInt(times[0]) * 2;
    int minuteInRange = times[1].equals("30") ? 1 : 0;
    return hourInRange + minuteInRange;
  }
}
