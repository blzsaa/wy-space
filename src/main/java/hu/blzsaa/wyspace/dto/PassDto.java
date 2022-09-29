package hu.blzsaa.wyspace.dto;

import java.util.Objects;

public class PassDto {
  private String name;
  private int strength;
  private int startTime;
  private int endTime;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getStrength() {
    return strength;
  }

  public void setStrength(int strength) {
    this.strength = strength;
  }

  public int getStartTime() {
    return startTime;
  }

  public void setStartTime(int startTime) {
    this.startTime = startTime;
  }

  public int getEndTime() {
    return endTime;
  }

  public void setEndTime(int endTime) {
    this.endTime = endTime;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    PassDto passDto = (PassDto) o;

    if (strength != passDto.strength) return false;
    if (startTime != passDto.startTime) return false;
    if (endTime != passDto.endTime) return false;
    return Objects.equals(name, passDto.name);
  }

  @Override
  public int hashCode() {
    int result = name != null ? name.hashCode() : 0;
    result = 31 * result + strength;
    result = 31 * result + startTime;
    result = 31 * result + endTime;
    return result;
  }

  @Override
  public String toString() {
    return "PassDto{"
        + "name='"
        + name
        + '\''
        + ", strength="
        + strength
        + ", startTime="
        + startTime
        + ", endTime="
        + endTime
        + '}';
  }
}
