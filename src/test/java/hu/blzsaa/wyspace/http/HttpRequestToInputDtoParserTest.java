package hu.blzsaa.wyspace.http;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

import com.google.cloud.functions.HttpRequest;
import hu.blzsaa.wyspace.dto.InputDto;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class HttpRequestToInputDtoParserTest {
  @Mock private HttpRequest httpRequest;
  @Mock private HttpRequest.HttpPart httpPart;
  @Mock private InputStream inputStream;
  private HttpRequestToInputDtoParser underTest;
  private AutoCloseable openMocks;

  @BeforeEach
  void setUp() throws IOException {
    openMocks = MockitoAnnotations.openMocks(this);

    doReturn(Optional.of("prefix;multipart/form-data;suffix"))
        .when(httpRequest)
        .getFirstHeader("Content-Type");
    doReturn(Map.of("does-not-matter", httpPart)).when(httpRequest).getParts();
    doReturn(inputStream).when(httpPart).getInputStream();
    doReturn(Optional.of("12")).when(httpRequest).getFirstQueryParameter("bandwidth");

    underTest = new HttpRequestToInputDtoParser();
  }

  @AfterEach
  void tearDown() throws Exception {
    openMocks.close();
  }

  @Test
  void parseShouldTakeInputStreamFromPartsAndBandwidthFromQuery() {
    // when
    var actual = underTest.parse(httpRequest);

    // then
    assertThat(actual).isEqualTo(new InputDto(inputStream, 12));
  }

  @Test
  void whenBandwidthIsMissingParseShouldAddZeroAsDefaultValue() {
    // given
    doReturn(Optional.empty()).when(httpRequest).getFirstQueryParameter("bandwidth");

    // when
    var actual = underTest.parse(httpRequest);

    // then
    assertThat(actual).isEqualTo(new InputDto(inputStream, 0));
  }

  @Test
  void headerContentTypeIsMandatory() {
    // given
    doReturn(Optional.empty()).when(httpRequest).getFirstHeader("Content-Type");

    // when
    var actual = catchThrowable(() -> underTest.parse(httpRequest));

    // then
    assertThat(actual)
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Content-Type header is mandatory and must contain: multipart/form-data");
  }

  @Test
  void contentTypeHeaderHasToContainFormData() {
    // given
    doReturn(Optional.of("multipart/not-form-data"))
        .when(httpRequest)
        .getFirstHeader("Content-Type");

    // when
    var actual = catchThrowable(() -> underTest.parse(httpRequest));

    // then
    assertThat(actual)
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Content-Type header is mandatory and must contain: multipart/form-data");
  }

  @Test
  void whenThereIsNoPartsInputStreamShouldBeZero() {
    // given
    doReturn(Map.of()).when(httpRequest).getParts();

    // when
    var actual = underTest.parse(httpRequest);

    // then
    assertThat(actual).isEqualTo(new InputDto(null, 12));
  }

  @Test
  void whenGetInputStreamThrowIOExceptionInputStreamShouldBeZero() throws IOException {
    // given
    doThrow(IOException.class).when(httpPart).getInputStream();

    // when
    var actual = underTest.parse(httpRequest);

    // then
    assertThat(actual).isEqualTo(new InputDto(null, 12));
  }
}
