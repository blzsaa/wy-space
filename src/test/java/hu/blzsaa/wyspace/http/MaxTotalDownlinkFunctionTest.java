package hu.blzsaa.wyspace.http;

import static org.hibernate.validator.internal.engine.path.PathImpl.createPathFromString;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.databind.json.JsonMapper;
import com.google.cloud.functions.HttpRequest;
import com.google.cloud.functions.HttpResponse;
import hu.blzsaa.wyspace.DownlinkService;
import hu.blzsaa.wyspace.dto.InputDto;
import hu.blzsaa.wyspace.dto.ResultDto;
import java.io.BufferedWriter;
import java.io.IOException;
import java.time.LocalTime;
import java.util.Map;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import org.jboss.logging.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class MaxTotalDownlinkFunctionTest {

  public static final String EXPECTED = "expected";
  @Mock private DownlinkService downlinkService;
  @Mock private HttpRequestToInputDtoParser httpRequestToInputDtoParser;
  @Mock private Logger log;

  @Mock private HttpRequest httpRequest;
  @Mock private HttpResponse httpResponse;
  @Mock private JsonMapper jsonMapper;
  @Mock private BufferedWriter writer;

  private MaxTotalDownlinkFunction underTest;

  private AutoCloseable openMocks;

  @BeforeEach
  void setUp() throws IOException {
    openMocks = MockitoAnnotations.openMocks(this);

    doReturn(writer).when(httpResponse).getWriter();

    underTest =
        new MaxTotalDownlinkFunction(downlinkService, httpRequestToInputDtoParser, jsonMapper, log);
  }

  @AfterEach
  void tearDown() throws Exception {
    openMocks.close();
  }

  @Test
  void serviceShouldFirstCallParserThenDownlinkService() throws Exception {
    // given
    InputDto inputDto = new InputDto(null, 12L);
    ResultDto resultDto = new ResultDto(LocalTime.NOON, LocalTime.NOON, true);
    doReturn(inputDto).when(httpRequestToInputDtoParser).parse(httpRequest);
    doReturn(resultDto).when(downlinkService).doTask(inputDto);
    doReturn(EXPECTED).when(jsonMapper).writeValueAsString(resultDto);

    // when
    underTest.service(httpRequest, httpResponse);

    // then
    verify(httpResponse).appendHeader("Content-Type", "application/json");
    verify(httpResponse).setStatusCode(200);
    verify(writer).write(EXPECTED);
  }

  @Test
  void serviceShouldCollectConstraintViolationsAndReturnThem() throws Exception {
    // given
    ConstraintViolationException exception =
        new ConstraintViolationException(
            "message",
            Set.of(
                mockConstraintViolation("field1", "message1"),
                mockConstraintViolation("field2", "message2"),
                mockConstraintViolation("field3", "message3")));
    doThrow(exception).when(httpRequestToInputDtoParser).parse(httpRequest);
    doReturn(EXPECTED)
        .when(jsonMapper)
        .writeValueAsString(
            Map.of(
                "violations",
                Set.of(
                    new ViolationDto("field1", "message1"),
                    new ViolationDto("field2", "message2"),
                    new ViolationDto("field3", "message3"))));

    // when
    underTest.service(httpRequest, httpResponse);

    // then
    verify(httpResponse).appendHeader("Content-Type", "application/json");
    verify(httpResponse).setStatusCode(400);
    verify(writer).write(EXPECTED);
    verify(log).info("validation problem", exception);
  }

  @Test
  void serviceShouldReturnWithHttpCode500InCaseOfAnyOtherException() throws Exception {
    // given
    IllegalArgumentException exception = new IllegalArgumentException("messageFromException");
    doThrow(exception).when(httpRequestToInputDtoParser).parse(httpRequest);
    doReturn(EXPECTED)
        .when(jsonMapper)
        .writeValueAsString(Map.of("message", "messageFromException"));

    // when
    underTest.service(httpRequest, httpResponse);

    // then
    verify(httpResponse).appendHeader("Content-Type", "application/json");
    verify(httpResponse).setStatusCode(500);
    verify(writer).write(EXPECTED);
    verify(log).info("unknown exception", exception);
  }

  private ConstraintViolation<?> mockConstraintViolation(String propertyPath, String message) {
    ConstraintViolation<?> e = mock(ConstraintViolation.class);
    doReturn(createPathFromString(propertyPath)).when(e).getPropertyPath();
    doReturn(message).when(e).getMessage();
    return e;
  }
}
