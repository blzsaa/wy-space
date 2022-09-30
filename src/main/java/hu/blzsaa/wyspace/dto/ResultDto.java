package hu.blzsaa.wyspace.dto;

import java.time.LocalTime;
import java.util.Objects;

public class ResultDto {
  private LocalTime startTime;
  private LocalTime endTime;
  private boolean downloadable;

  public LocalTime getStartTime() {
    return startTime;
  }

  public void setStartTime(LocalTime startTime) {
    this.startTime = startTime;
  }

  public LocalTime getEndTime() {
    return endTime;
  }

  public void setEndTime(LocalTime endTime) {
    this.endTime = endTime;
  }

  public void setDownloadable(boolean downloadable) {
    this.downloadable = downloadable;
  }

  public boolean isDownloadable() {
    return downloadable;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    ResultDto resultDto = (ResultDto) o;

    if (downloadable != resultDto.downloadable) return false;
    if (!Objects.equals(startTime, resultDto.startTime)) return false;
    return Objects.equals(endTime, resultDto.endTime);
  }

  @Override
  public int hashCode() {
    int result = startTime != null ? startTime.hashCode() : 0;
    result = 31 * result + (endTime != null ? endTime.hashCode() : 0);
    result = 31 * result + (downloadable ? 1 : 0);
    return result;
  }

  @Override
  public String toString() {
    return "ResultDto{"
        + "startTime="
        + startTime
        + ", endTime="
        + endTime
        + ", downloadable="
        + downloadable
        + '}';
  }
}
