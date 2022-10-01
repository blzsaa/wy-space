package hu.blzsaa.wyspace;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DownloadPredicatorTest {
  private DownloadPredicator underTest;

  @BeforeEach
  void setUp() {
    underTest = new DownloadPredicator();
  }

  @Test
  void emptyRangeIsAlwaysDownloadable() {
    assertThat(underTest.isDownloadable(List.of(), 0)).isTrue();
  }

  @Test
  void rangeShouldBeDownloadableWhenAllElementsOfListIsNotGreaterThanTheMaxDownloadSpeed() {
    assertThat(underTest.isDownloadable(List.of(1L, 2L, 3L, 4L, 3L), 4)).isTrue();
  }

  @Test
  void
      rangeShouldNotBeDownloadableWhenThereIsAnElementInTheListWhichIsGreaterThanTheMaxDownloadSpeed() {
    assertThat(underTest.isDownloadable(List.of(1L, 2L, 3L, 4L, 3L), 3)).isFalse();
  }
}
