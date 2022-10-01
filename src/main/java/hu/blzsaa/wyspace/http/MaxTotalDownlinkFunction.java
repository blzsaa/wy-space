package hu.blzsaa.wyspace.http;

import com.google.cloud.functions.HttpFunction;
import com.google.cloud.functions.HttpRequest;
import com.google.cloud.functions.HttpResponse;
import com.google.gson.Gson;
import hu.blzsaa.wyspace.DownlinkService;
import hu.blzsaa.wyspace.dto.ResultDto;
import java.io.Writer;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import javax.validation.ConstraintViolationException;
import org.jboss.logging.Logger;

@Named("httpFunction")
@ApplicationScoped
public class MaxTotalDownlinkFunction implements HttpFunction {

  private final DownlinkService downlinkService;
  private final HttpRequestToInputDtoParser httpRequestToInputDtoParser;

  private final Gson gson;
  private final Logger log;

  public MaxTotalDownlinkFunction(
      DownlinkService downlinkService,
      HttpRequestToInputDtoParser httpRequestToInputDtoParser,
      Gson gson,
      Logger log) {
    this.downlinkService = downlinkService;
    this.httpRequestToInputDtoParser = httpRequestToInputDtoParser;
    this.gson = gson;
    this.log = log;
  }

  @Override
  public void service(HttpRequest httpRequest, HttpResponse httpResponse) throws Exception {
    Writer writer = httpResponse.getWriter();
    httpResponse.appendHeader("Content-Type", "application/json");
    try {
      var input = httpRequestToInputDtoParser.parse(httpRequest);
      ResultDto resultDto = downlinkService.doTask(input);
      httpResponse.setStatusCode(200);
      writer.write(gson.toJson(resultDto));
    } catch (ConstraintViolationException e) {
      httpResponse.setStatusCode(400);
      var violations = collectViolations(e);
      log.info("validation problem", e);
      writer.write(gson.toJson(Map.of("violations", violations)));
    } catch (Exception e) {
      httpResponse.setStatusCode(500);
      writer.write(gson.toJson(Map.of("message", e.getMessage())));
      log.info("unknown exception", e);
    }
  }

  private static Set<ViolationDto> collectViolations(ConstraintViolationException e) {
    return e.getConstraintViolations().stream()
        .map(
            c -> {
              ViolationDto violation = new ViolationDto();
              violation.setField(c.getPropertyPath().toString());
              violation.setMessage(c.getMessage());
              return violation;
            })
        .collect(Collectors.toSet());
  }
}
