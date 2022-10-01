package hu.blzsaa.wyspace.it;

import static hu.blzsaa.wyspace.it.ITHelper.resultDtoFrom;
import static hu.blzsaa.wyspace.it.ITHelper.send;

import hu.blzsaa.wyspace.dto.ResultDto;
import io.restassured.RestAssured;
import io.restassured.config.ObjectMapperConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.mapper.ObjectMapperType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalTime;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@Testcontainers
class PerfIT {
  private static final Logger LOGGER = LoggerFactory.getLogger(PerfIT.class);
  private @TempDir Path tempDir;

  @SuppressWarnings("resource")
  @Container
  public GenericContainer<?> container =
      new GenericContainer<>(DockerImageName.parse("wy-space:latest"))
          .withEnv("JAVA_OPTS", "-Xmx100m -Xms100m -XX:PermSize=100m -XX:MaxPermSize=200m -Xss100m")
          .withExposedPorts(8080);

  @SuppressWarnings({"java:S2696"})
  @BeforeEach
  void setUp() {
    Slf4jLogConsumer logConsumer = new Slf4jLogConsumer(LOGGER);
    container.followOutput(logConsumer);

    RestAssured.baseURI = "http://" + container.getHost();
    RestAssured.port = container.getMappedPort(8080);
    RestAssured.config =
        RestAssuredConfig.config()
            .objectMapperConfig(new ObjectMapperConfig(ObjectMapperType.GSON));
  }

  @Test
  //  @JvmOptions("-Xmx100m -Xms100m -XX:PermSize=100m -XX:MaxPermSize=200m -Xss100m")
  void solvingForBigInputFileShouldNotThrowOutOfMemoryException() throws IOException {
    // given
    Path tempFile = tempDir.resolve("largeFile");
    try (var br = new BufferedWriter(new FileWriter(tempFile.toAbsolutePath().toString()))) {
      for (int i = 0; i < 1_000_000; i++) {
        br.append("RedDwarf,2,00:00,01:30\n");
      }
    }

    ExtractableResponse<Response> actual = send(tempFile.toAbsolutePath().toString(), "12");

    Assertions.assertThat(actual.statusCode()).isEqualTo(200);
    Assertions.assertThat(actual.body().as(ResultDto.class))
        .isEqualTo(resultDtoFrom(LocalTime.of(0, 0), LocalTime.of(0, 30), false));
  }
}
