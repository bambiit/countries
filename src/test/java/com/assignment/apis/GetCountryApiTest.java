package com.assignment.apis;

import com.assignment.utils.ApplicationTestUtils;
import com.github.tomakehurst.wiremock.client.WireMock;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.test.context.ActiveProfiles;
import wiremock.org.apache.commons.io.IOUtils;

import java.nio.charset.StandardCharsets;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureWireMock(port = 0)
public class GetCountryApiTest {

    @LocalServerPort
    private int port;

    @Autowired
    private CircuitBreakerRegistry circuitBreakerRegistry;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
    }

    @AfterEach
    public void tearDown() {
        circuitBreakerRegistry.circuitBreaker("remote-broken").reset();
    }

    @Test
    @SneakyThrows
    public void testGetCountry_whenValidResponse_thenResponse() {
        WireMock.stubFor(WireMock.get("/name/Finland")
                .willReturn(WireMock.aResponse()
                        .withStatus(OK.value())
                        .withHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                        .withBodyFile("get-country-finland.json")));


        JsonPath expectedResponse =
                new JsonPath(
                        IOUtils.toString(
                                GetAllCountriesApiTest.class.
                                        getResourceAsStream("/expected-response/get-country-finland.json"),
                                StandardCharsets.UTF_8.toString()));

        ApplicationTestUtils.getNewRequest().get("/api/countries/Finland")
                .then()
                .log()
                .all()
                .statusCode(OK.value())
                .body("", equalTo(expectedResponse.getMap("")));
    }

    @Test
    @SneakyThrows
    public void testGetCountry_whenMultiResponse_thenNotFoundResponse() {
        WireMock.stubFor(WireMock.get("/name/Fi")
                .willReturn(WireMock.aResponse()
                        .withStatus(OK.value())
                        .withHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                        .withBodyFile("get-country-fi.json")));


        ApplicationTestUtils.getNewRequest().get("/api/countries/Fi")
                .then()
                .log()
                .all()
                .statusCode(NOT_FOUND.value())
                .noRootPath();
    }

    @Test
    @SneakyThrows
    public void testGetCountry_whenEmptyResponse_thenNotFoundResponse() {

        //literally empty response
        WireMock.stubFor(WireMock.get("/name/Finland")
                .willReturn(WireMock.aResponse()
                        .withStatus(OK.value())));


        ApplicationTestUtils.getNewRequest().get("/api/countries/Finland")
                .then()
                .log()
                .all()
                .statusCode(NOT_FOUND.value())
                .noRootPath();

        // empty array response
        WireMock.stubFor(WireMock.get("/name/Finland")
                .willReturn(WireMock.aResponse()
                        .withStatus(OK.value())
                        .withHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                        .withBody("[]")));


        ApplicationTestUtils.getNewRequest().get("/api/countries/Finland")
                .then()
                .log()
                .all()
                .statusCode(NOT_FOUND.value())
                .noRootPath();
    }

    @Test
    @SneakyThrows
    public void testGetCountry_whenInvalidResponseReceived_thenErrorResponse() {
        WireMock.stubFor(WireMock.get("/name/Finland")
                .willReturn(WireMock.aResponse()
                        .withStatus(INTERNAL_SERVER_ERROR.value())));

        JsonPath expectedResponse =
                new JsonPath(
                        IOUtils.toString(
                                GetAllCountriesApiTest.class.
                                        getResourceAsStream("/expected-response/internal-server-error.json"),
                                StandardCharsets.UTF_8.toString()));


        ApplicationTestUtils.getNewRequest().get("/api/countries/Finland")
                .then()
                .log()
                .all()
                .statusCode(INTERNAL_SERVER_ERROR.value())
                .body("", equalTo(expectedResponse.getMap("")));
    }
}
