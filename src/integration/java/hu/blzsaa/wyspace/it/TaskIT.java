package hu.blzsaa.wyspace.it;

import static org.assertj.core.api.Assertions.assertThat;

import hu.blzsaa.wyspace.Main;
import hu.blzsaa.wyspace.Task;
import hu.blzsaa.wyspace.exception.NoRangeFoundException;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
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
  void emptyFileShouldReturnWithNoRangeFoundException(@TempDir Path tempDir) throws IOException {
    // given
    Path emptyFile = tempDir.resolve("emptyFile");
    Files.write(emptyFile, List.of(""));

    String[] args = {emptyFile.toAbsolutePath().toString()};

    // when
    Throwable actual = Assertions.catchThrowable(() -> task.doTask(args));

    // then
    assertThat(actual).isInstanceOf(NoRangeFoundException.class);
  }

  @Test
  void solvingTestInputShouldReturn20AKA1000To1030() throws IOException {
    // when
    var actual = task.doTask(new String[] {"src/integration/resources/pass-schedule.txt"});

    // then
    assertThat(actual).isEqualTo(20);
  }

  @Test
  @JvmOptions("-Xmx10m -Xms10m -XX:PermSize=10m -XX:MaxPermSize=20m -Xss10m")
  void solveForBigInputFile() throws IOException {
    // given
    Path emptyFile = tempDir.resolve("largeFile");
    try (var br = new BufferedWriter(new FileWriter(emptyFile.toAbsolutePath().toString()))) {
      for (int i = 0; i < 1_000_000; i++) {
        br.append("RedDwarf,2,00:00,01:30\n");
      }
    }
    String[] args = {emptyFile.toAbsolutePath().toString()};

    // when
    var actual = task.doTask(args);

    // then
    assertThat(actual).isEqualTo(0);
  }
}
