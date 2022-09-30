package hu.blzsaa.wyspace.it;

import static org.assertj.core.api.Assertions.assertThat;

import hu.blzsaa.wyspace.Main;
import hu.blzsaa.wyspace.Task;
import hu.blzsaa.wyspace.dto.ResultDto;
import hu.blzsaa.wyspace.exception.NoRangeFoundException;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalTime;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.quickperf.junit5.QuickPerfTest;
import org.quickperf.jvm.annotations.JvmOptions;

@QuickPerfTest
class TaskIT {

  private final Task task = Main.dummyDI();
  private @TempDir Path tempDir;

  @Test
  void emptyFileShouldReturnWithNoRangeFoundException() throws IOException {
    // given
    Path tempFile = tempDir.resolve("emptyFile");
    Files.write(tempFile, List.of(""));

    String[] args = {tempFile.toAbsolutePath().toString(), "12"};

    // when
    Throwable actual = Assertions.catchThrowable(() -> task.doTask(args));

    // then
    assertThat(actual).isInstanceOf(NoRangeFoundException.class);
  }

  @Test
  void solvingTestInputShouldReturn1000To1030AndNotDownloadableIfBandwidthIsLessThen45()
      throws IOException {
    // when
    var actual = task.doTask(createArgs("pass-schedule.txt", 44));

    // then
    assertThat(actual).isEqualTo(resultDtoFrom(LocalTime.of(10, 0), LocalTime.of(10, 30), false));
  }

  @Test
  void solvingTestInputShouldReturn1000To1030AndIDownloadableIfBandwidthIs45rGreater()
      throws IOException {
    // when
    var actual = task.doTask(createArgs("pass-schedule.txt", 45));

    // then
    assertThat(actual).isEqualTo(resultDtoFrom(LocalTime.of(10, 0), LocalTime.of(10, 30), true));
  }

  @Test
  void passesCanStartsAndEndOnOtherThan30Or00() throws IOException {
    // when
    var actual = task.doTask(createArgs("pass-schedule-any-minute.txt"));

    // then
    assertThat(actual).isEqualTo(resultDtoFrom(LocalTime.of(10, 2), LocalTime.of(10, 32), false));
  }

  @Test
  void passesCanHappenNonPeriodicallyAsWell() throws IOException {
    // when
    var actual = task.doTask(createArgs("pass-schedule-with-changing-speed.txt"));

    // then
    assertThat(actual).isEqualTo(resultDtoFrom(LocalTime.of(2, 31), LocalTime.of(3, 1), true));
  }

  @Test
  @JvmOptions("-Xmx10m -Xms10m -XX:PermSize=10m -XX:MaxPermSize=20m -Xss10m")
  void solvingForBigInputFileShouldNotThrowOutOfMemoryException() throws IOException {
    // given
    Path tempFile = tempDir.resolve("largeFile");
    try (var br = new BufferedWriter(new FileWriter(tempFile.toAbsolutePath().toString()))) {
      for (int i = 0; i < 1_000_000; i++) {
        br.append("RedDwarf,2,00:00,01:30\n");
      }
    }
    String[] args = {tempFile.toAbsolutePath().toString(), "12"};

    // when
    var actual = task.doTask(args);

    // then
    assertThat(actual).isEqualTo(resultDtoFrom(LocalTime.of(0, 0), LocalTime.of(0, 30), false));
  }

  private static String[] createArgs(String fileName) {
    return createArgs(fileName, 12L);
  }

  private static String[] createArgs(String fileName, long bandwidth) {
    return new String[] {"src/integration/resources/" + fileName, "" + bandwidth};
  }

  ResultDto resultDtoFrom(LocalTime startTime, LocalTime endTime, boolean downloadable) {
    ResultDto resultDto = new ResultDto();
    resultDto.setStartTime(startTime);
    resultDto.setEndTime(endTime);
    resultDto.setDownloadable(downloadable);
    return resultDto;
  }
}
