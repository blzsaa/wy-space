package hu.blzsaa.wyspace.dto;

import java.io.InputStream;
import java.util.Objects;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class InputDto {
  @NotNull private InputStream inputStream;

  @Min(0)
  private long bandwidth;

  public InputStream getInputStream() {
    return inputStream;
  }

  public void setInputStream(InputStream inputStream) {
    this.inputStream = inputStream;
  }

  public long getBandwidth() {
    return bandwidth;
  }

  public void setBandwidth(long bandwidth) {
    this.bandwidth = bandwidth;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    InputDto inputDto = (InputDto) o;

    if (bandwidth != inputDto.bandwidth) return false;
    return Objects.equals(inputStream, inputDto.inputStream);
  }

  @Override
  public int hashCode() {
    int result = inputStream != null ? inputStream.hashCode() : 0;
    result = 31 * result + (int) (bandwidth ^ (bandwidth >>> 32));
    return result;
  }

  @Override
  public String toString() {
    return "InputDto{" + "inputStream=" + inputStream + ", bandwidth=" + bandwidth + '}';
  }
}
