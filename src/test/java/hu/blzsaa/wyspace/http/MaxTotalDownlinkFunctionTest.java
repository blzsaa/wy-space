package hu.blzsaa.wyspace.http;

import static org.hibernate.validator.internal.engine.path.PathImpl.createPathFromString;
import static org.mockito.Mockito.*;

import com.google.cloud.functions.HttpRequest;
import com.google.cloud.functions.HttpResponse;
import com.google.gson.Gson;
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
  @Mock private Gson gson;
  @Mock private BufferedWriter writer;

  private MaxTotalDownlinkFunction underTest;

  private AutoCloseable openMocks;

  @BeforeEach
  void setUp() throws IOException {
    openMocks = MockitoAnnotations.openMocks(this);

    doReturn(writer).when(httpResponse).getWriter();

    underTest =
        new MaxTotalDownlinkFunction(downlinkService, httpRequestToInputDtoParser, gson, log);
  }

  @AfterEach
  void tearDown() throws Exception {
    openMocks.close();
  }

  @Test
  void serviceShouldFirstCallParserThenDownlinkService() throws Exception {
    // given
    InputDto inputDto = new InputDto();
    inputDto.setBandwidth(12L);
    ResultDto resultDto = new ResultDto();
    resultDto.setStartTime(LocalTime.NOON);
    doReturn(inputDto).when(httpRequestToInputDtoParser).parse(httpRequest);
    doReturn(resultDto).when(downlinkService).doTask(inputDto);
    doReturn(EXPECTED).when(gson).toJson(resultDto);

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
        .when(gson)
        .toJson(
            Map.of(
                "violations",
                Set.of(
                    createViolationDto("field1", "message1"),
                    createViolationDto("field2", "message2"),
                    createViolationDto("field3", "message3"))));

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
    doReturn(EXPECTED).when(gson).toJson(Map.of("message", "messageFromException"));

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

  private ViolationDto createViolationDto(String field, String message) {
    var dto = new ViolationDto();
    dto.setField(field);
    dto.setMessage(message);
    return dto;
  }
}
