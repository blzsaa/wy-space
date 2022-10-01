package hu.blzsaa.wyspace.http;

import java.util.Objects;

public class ViolationDto {
  private String field;
  private String message;

  public String getField() {
    return field;
  }

  public void setField(String field) {
    this.field = field;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    ViolationDto that = (ViolationDto) o;

    if (!Objects.equals(field, that.field)) return false;
    return Objects.equals(message, that.message);
  }

  @Override
  public int hashCode() {
    int result = field != null ? field.hashCode() : 0;
    result = 31 * result + (message != null ? message.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return "ViolationDto{" + "field='" + field + '\'' + ", message='" + message + '\'' + '}';
  }
}
