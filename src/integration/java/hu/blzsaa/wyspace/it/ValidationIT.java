package hu.blzsaa.wyspace.it;

import static hu.blzsaa.wyspace.it.ITHelper.resultDtoFrom;
import static io.restassured.RestAssured.given;

import hu.blzsaa.wyspace.dto.ResultDto;
import io.restassured.RestAssured;
import io.restassured.config.ObjectMapperConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.mapper.ObjectMapperType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.time.LocalTime;
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
class ValidationIT {

  private static final Logger LOGGER = LoggerFactory.getLogger(ValidationIT.class);

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

  @Test
  void bandwidthMustNotBaANegativeNumber() {
    // missing  input, header
    ExtractableResponse<Response> actual =
        ITHelper.send("src/integration/resources/pass-schedule.txt", "-1");

    Assertions.assertThat(actual.statusCode()).isEqualTo(400);
    Assertions.assertThat(actual.body().asString())
        .contains(
            "{\"violations\":[{\"field\":\"doTask.inputDto.bandwidth\",\"message\":\"must be greater than or equal to 0\"}]}");
  }

  @Test
  void bandwidthShouldBeOptionalMissingOneShouldCountAsZero() {
    ExtractableResponse<Response> actual =
        ITHelper.send("src/integration/resources/pass-schedule.txt");
    Assertions.assertThat(actual.statusCode()).isEqualTo(200);
    ResultDto expected = resultDtoFrom(LocalTime.of(10, 0), LocalTime.of(10, 30), false);
    Assertions.assertThat(actual.body().as(ResultDto.class)).isEqualTo(expected);
  }

  @Test
  void fileShouldBeMandatory() {
    ExtractableResponse<Response> actual =
        given().header("Content-Type", "multipart/form-data").when().post("/").then().extract();
    Assertions.assertThat(actual.statusCode()).isEqualTo(400);
    Assertions.assertThat(actual.body().asString())
        .contains(
            "{\"violations\":[{\"field\":\"doTask.inputDto.inputStream\",\"message\":\"must not be null\"}]}");
  }
}
