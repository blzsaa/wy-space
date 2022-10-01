package hu.blzsaa.wyspace.http;

import com.google.cloud.functions.HttpRequest;
import hu.blzsaa.wyspace.dto.InputDto;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.Map;
import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class HttpRequestToInputDtoParser {

  InputDto parse(HttpRequest httpRequest) {
    var inputDto = new InputDto();
    inputDto.setInputStream(getInputStreamReader(httpRequest));
    inputDto.setBandwidth(getBandwidth(httpRequest));
    return inputDto;
  }

  private InputStream getInputStreamReader(HttpRequest httpRequest) {
    if (httpRequest
        .getFirstHeader("Content-Type")
        .filter(b -> b.contains("multipart/form-data"))
        .isEmpty()) {
      throw new IllegalArgumentException(
          "Content-Type header is mandatory and must contain: multipart/form-data");
    }
    try {
      Map<String, HttpRequest.HttpPart> parts = httpRequest.getParts();
      if (parts.isEmpty()) {
        return null;
      }
      return parts.values().iterator().next().getInputStream();
    } catch (UncheckedIOException | IOException e) {
      return null;
    }
  }

  private long getBandwidth(HttpRequest httpRequest) {
    return httpRequest.getFirstQueryParameter("bandwidth").map(Long::parseLong).orElse(0L);
  }
}
