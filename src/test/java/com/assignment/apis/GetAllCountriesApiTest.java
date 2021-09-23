package com.assignment.apis;

import com.assignment.utils.ApplicationTestUtils;
import com.github.tomakehurst.wiremock.client.WireMock;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.test.context.ActiveProfiles;
import wiremock.org.apache.commons.io.IOUtils;

import java.nio.charset.StandardCharsets;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureWireMock(port = 0)
public class GetAllCountriesApiTest {

    @LocalServerPort
    private int port;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
    }

    @Test
    @SneakyThrows
    public void testGetAllCountries_whenValidResponseReceived_thenAllResponse() {

        WireMock.stubFor(WireMock.get("/all")
                .willReturn(WireMock.aResponse()
                        .withStatus(OK.value())
                        .withHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                        .withBodyFile("get-all-countries-valid.json")));


        JsonPath expectedResponse =
                new JsonPath(
                        IOUtils.toString(
                                GetAllCountriesApiTest.class.
                                        getResourceAsStream("/expected-response/get-all-countries.json"),
                                StandardCharsets.UTF_8.toString()));

        ApplicationTestUtils.getNewRequest().get("/api/countries/")
                .then()
                .log()
                .all()
                .statusCode(OK.value())
                .body("", equalTo(expectedResponse.getMap("")));
    }

    @Test
    @SneakyThrows
    public void testGetAllCountries_whenInvalidResponseReceived_thenErrorResponse() {

        WireMock.stubFor(WireMock.get("/all")
                .willReturn(WireMock.aResponse()
                        .withStatus(INTERNAL_SERVER_ERROR.value())));

        JsonPath expectedResponse =
                new JsonPath(
                        IOUtils.toString(
                                GetAllCountriesApiTest.class.
                                        getResourceAsStream("/expected-response/internal-server-error.json"),
                                StandardCharsets.UTF_8.toString()));

        ApplicationTestUtils.getNewRequest().get("/api/countries/")
                .then()
                .log()
                .all()
                .statusCode(INTERNAL_SERVER_ERROR.value())
                .body("", equalTo(expectedResponse.getMap("")));
    }

    @Test
    @SneakyThrows
    public void testGetAllCountries_whenEmptyResponseReceived_thenResponse() {

        JsonPath expectedResponse =
                new JsonPath(
                        IOUtils.toString(
                                GetAllCountriesApiTest.class.
                                        getResourceAsStream("/expected-response/get-all-countries-empty.json"),
                                StandardCharsets.UTF_8.toString()));


        // literally empty response
        WireMock.stubFor(WireMock.get("/all")
                .willReturn(WireMock.aResponse()
                        .withStatus(OK.value())));

        ApplicationTestUtils.getNewRequest().get("/api/countries/")
                .then()
                .log()
                .all()
                .statusCode(OK.value())
                .body("", equalTo(expectedResponse.getMap("")));


        // literally empty array
        WireMock.stubFor(WireMock.get("/all")
                .willReturn(WireMock.aResponse()
                        .withStatus(OK.value())
                        .withHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                        .withBody("[]")));

        ApplicationTestUtils.getNewRequest().get("/api/countries/")
                .then()
                .log()
                .all()
                .statusCode(OK.value())
                .body("", equalTo(expectedResponse.getMap("")));

    }

}
