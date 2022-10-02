package hu.blzsaa.wyspace.fileinterpreter;

import hu.blzsaa.wyspace.dto.PassDto;
import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import javax.validation.Valid;

@ApplicationScoped
class LineParser {

  public PassDto parseLine(@Valid FileLineDto fileLineDto) {

    List<String> fields = fileLineDto.fields();
    return new PassDto(
        Integer.parseInt(fields.get(fields.size() - 3)),
        parseTime(fields.get(fields.size() - 2)),
        parseTime(fields.get(fields.size() - 1)));
  }

  private int parseTime(String time) {
    var times = time.split(":");
    int hour = Integer.parseInt(times[0]);
    int minute = Integer.parseInt(times[1]);
    return hour * 60 + minute;
  }
}
