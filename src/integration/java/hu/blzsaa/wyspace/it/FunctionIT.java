package hu.blzsaa.wyspace.it;

import static hu.blzsaa.wyspace.it.ITHelper.resultDtoFrom;

import hu.blzsaa.wyspace.dto.ResultDto;
import io.restassured.RestAssured;
import io.restassured.config.ObjectMapperConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.mapper.ObjectMapperType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@Testcontainers
class FunctionIT {

  private static final Logger LOGGER = LoggerFactory.getLogger(FunctionIT.class);

  @SuppressWarnings("resource")
  @Container
  public GenericContainer<?> container =
      new GenericContainer<>(DockerImageName.parse("wy-space:latest")).withExposedPorts(8080);

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

  @SuppressWarnings("unchecked")
  @Test
  void emptyFileShouldReturnWithNoRangeFoundException() {
    ExtractableResponse<Response> actual = send("emptyFile.txt", "44");

    Assertions.assertThat(actual.statusCode()).isEqualTo(500);
    Assertions.assertThat(actual.body().as(HashMap.class))
        .isEqualTo(Map.of("message", "could not found maximum range"));
  }

  @Test
  void solvingTestInputShouldReturn1000To1030AndNotDownloadableIfBandwidthIsLessThen45() {
    ExtractableResponse<Response> actual = send("pass-schedule.txt", "44");

    Assertions.assertThat(actual.statusCode()).isEqualTo(200);
    ResultDto expected = resultDtoFrom(LocalTime.of(10, 0), LocalTime.of(10, 30), false);
    Assertions.assertThat(actual.body().as(ResultDto.class)).isEqualTo(expected);
  }

  @Test
  void solvingTestInputShouldReturn1000To1030AndIDownloadableIfBandwidthIs45rGreater() {
    ExtractableResponse<Response> actual = send("pass-schedule.txt", "45");

    Assertions.assertThat(actual.statusCode()).isEqualTo(200);
    ResultDto expected = resultDtoFrom(LocalTime.of(10, 0), LocalTime.of(10, 30), true);
    Assertions.assertThat(actual.body().as(ResultDto.class)).isEqualTo(expected);
  }

  @Test
  void passesCanStartsAndEndOnOtherThan30Or00() {
    ExtractableResponse<Response> actual = send("pass-schedule-any-minute.txt", "12");

    Assertions.assertThat(actual.statusCode()).isEqualTo(200);
    ResultDto expected = resultDtoFrom(LocalTime.of(10, 2), LocalTime.of(10, 32), false);
    Assertions.assertThat(actual.body().as(ResultDto.class)).isEqualTo(expected);
  }

  @Test
  void passesCanHappenNonPeriodicallyAsWell() {
    ExtractableResponse<Response> actual = send("pass-schedule-with-changing-speed.txt", "45");

    Assertions.assertThat(actual.statusCode()).isEqualTo(200);
    ResultDto expected = resultDtoFrom(LocalTime.of(2, 31), LocalTime.of(3, 1), true);
    Assertions.assertThat(actual.body().as(ResultDto.class)).isEqualTo(expected);
  }

  private ExtractableResponse<Response> send(String pathname, String bandwidth) {
    return ITHelper.send("src/integration/resources/" + pathname, bandwidth);
  }
}
