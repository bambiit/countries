package com.assignment.repositories;

import com.assignment.exceptions.NotFoundCountryException;
import com.assignment.exceptions.TechnicalException;
import com.assignment.services.repositories.CountryRepository;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.test.context.ActiveProfiles;
import reactor.test.StepVerifier;

import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureWireMock(port = 0)
public class RestCountriesEuRepositoryTest {

    private static final int HTTP_OK = OK.value();
    private static final int HTTP_INTERNAL_SERVER_ERROR = INTERNAL_SERVER_ERROR.value();
    @Autowired
    private CountryRepository repository;

    @Test
    public void testGetAllCountries_whenValidResponseReceived_thenAllResponse() {

        WireMock.stubFor(WireMock.get("/all")
                .willReturn(WireMock.aResponse()
                        .withStatus(HTTP_OK)
                        .withHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                        .withBodyFile("get-all-countries-valid.json")));

        StepVerifier.create(repository.getAllCountries())
                .assertNext(items -> {
                    Assertions.assertThat(items.size()).isEqualTo(2);

                    items.forEach(item -> {
                        switch (item.getName()) {
                            case "Afghanistan":
                                Assertions.assertThat(item.getPopulation()).isEqualTo(27657145);
                                Assertions.assertThat(item.getCapital()).isEqualTo("Kabul");
                                Assertions.assertThat(item.getCountryCode()).isEqualTo("AF");
                                Assertions.assertThat(item.getFlagFileUrl())
                                        .isEqualTo("https://restcountries.eu/data/afg.svg");
                                break;
                            case "Åland Islands":
                                Assertions.assertThat(item.getPopulation()).isEqualTo(28875);
                                Assertions.assertThat(item.getCapital()).isEqualTo("Mariehamn");
                                Assertions.assertThat(item.getCountryCode()).isEqualTo("AX");
                                Assertions.assertThat(item.getFlagFileUrl())
                                        .isEqualTo("https://restcountries.eu/data/ala.svg");
                                break;
                        }

                    });

                })
                .verifyComplete();
    }

    @Test
    public void testGetAllCountries_whenInValidResponseReceived_thenGetException() {
        WireMock.stubFor(WireMock.get("/all")
                .willReturn(WireMock.aResponse().withStatus(HTTP_INTERNAL_SERVER_ERROR)));

        StepVerifier.create(repository.getAllCountries())
                .expectError(TechnicalException.class)
                .verify();

    }

    @Test
    public void testGetAllCountries_whenEmptyResponseReceived_thenEmptyResponse() {
        // literally empty response
        WireMock.stubFor(WireMock.get("/all")
                .willReturn(WireMock.aResponse().withStatus(HTTP_OK)));

        StepVerifier.create(repository.getAllCountries())
                .assertNext(items -> Assertions.assertThat(items.size()).isEqualTo(0));

        // response with empty array
        WireMock.stubFor(WireMock.get("/all")
                .willReturn(WireMock.aResponse()
                        .withStatus(HTTP_OK)
                        .withBody("[]")));

        StepVerifier.create(repository.getAllCountries())
                .assertNext(items -> Assertions.assertThat(items.size()).isEqualTo(0));

    }

    @Test
    public void testGetCountry_whenInValidResponseReceived_thenGetException() {
        WireMock.stubFor(WireMock.get("/name/Finland")
                .willReturn(WireMock.aResponse().withStatus(HTTP_INTERNAL_SERVER_ERROR)));

        StepVerifier.create(repository.getCountry("Finland"))
                .expectError(TechnicalException.class)
                .verify();

    }

    @Test
    public void testGetCountry_whenValidResponseReceived_thenResponse() {
        WireMock.stubFor(WireMock.get("/name/Finland")
                .willReturn(WireMock.aResponse()
                        .withStatus(HTTP_OK)
                        .withHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                        .withBodyFile("get-country-finland.json")));

        StepVerifier.create(repository.getCountry("Finland"))
                .assertNext(item -> {
                    Assertions.assertThat(item.getName()).isEqualTo("Finland");
                    Assertions.assertThat(item.getPopulation()).isEqualTo(5491817);
                    Assertions.assertThat(item.getCapital()).isEqualTo("Helsinki");
                    Assertions.assertThat(item.getCountryCode()).isEqualTo("FI");
                    Assertions.assertThat(item.getFlagFileUrl())
                            .isEqualTo("https://restcountries.eu/data/fin.svg");
                })
                .verifyComplete();
    }

    @Test
    public void testGetCountry_whenEmptyResponseReceived_thenNotFoundCountryException() {
        // literally empty response
        WireMock.stubFor(WireMock.get("/name/Finland")
                .willReturn(WireMock.aResponse()
                        .withStatus(HTTP_OK)));

        StepVerifier.create(repository.getCountry("Finland"))
                .verifyError(NotFoundCountryException.class);


        // empty array response
        WireMock.stubFor(WireMock.get("/name/Finland")
                .willReturn(WireMock.aResponse()
                        .withStatus(HTTP_OK)
                        .withHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                        .withBody("[]")));

        StepVerifier.create(repository.getCountry("Finland"))
                .verifyError(NotFoundCountryException.class);
    }

    @Test
    public void testGetCountry_whenMultiCountriesResponseReceived_thenNotFoundCountryException() {
        WireMock.stubFor(WireMock.get("/name/Fi")
                .willReturn(WireMock.aResponse()
                        .withStatus(HTTP_OK)
                        .withHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                        .withBodyFile("get-country-fi.json")));

        StepVerifier.create(repository.getCountry("Fi")).verifyError(NotFoundCountryException.class);
    }


}
