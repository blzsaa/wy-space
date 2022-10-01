package hu.blzsaa.wyspace.it;

import static io.restassured.RestAssured.given;

import hu.blzsaa.wyspace.dto.ResultDto;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.io.File;
import java.time.LocalTime;

final class ITHelper {

  private ITHelper() {}

  static ExtractableResponse<Response> send(String pathname, String bandwidth) {
    return given()
        .when()
        .multiPart("file", new File(pathname), "text/html")
        .param("bandwidth", bandwidth)
        .post("/")
        .then()
        .extract();
  }

  static ExtractableResponse<Response> send(String pathname) {
    return given()
        .when()
        .multiPart("file", new File(pathname), "text/html")
        .post("/")
        .then()
        .extract();
  }

  static ResultDto resultDtoFrom(LocalTime startTime, LocalTime endTime, boolean downloadable) {
    ResultDto resultDto = new ResultDto();
    resultDto.setStartTime(startTime);
    resultDto.setEndTime(endTime);
    resultDto.setDownloadable(downloadable);
    return resultDto;
  }
}
