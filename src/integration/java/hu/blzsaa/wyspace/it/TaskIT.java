package hu.blzsaa.wyspace.it;

import static org.assertj.core.api.Assertions.assertThat;

import hu.blzsaa.wyspace.Main;
import hu.blzsaa.wyspace.Task;
import hu.blzsaa.wyspace.exception.NoRangeFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class TaskIT {

  private final Task task = Main.dummyDI();

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
}
