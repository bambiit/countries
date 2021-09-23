package com.assignment.repositories;

import com.assignment.exceptions.NotFoundCountryException;
import com.assignment.exceptions.TechnicalException;
import com.assignment.services.models.Country;
import com.assignment.services.repositories.CountryRepository;
import lombok.AllArgsConstructor;
import org.springframework.cloud.client.circuitbreaker.ReactiveCircuitBreaker;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

@Repository
@AllArgsConstructor
public class RestCountriesEuRepository implements CountryRepository {

    private final WebClient webClient;

    private final ReactiveCircuitBreaker circuitBreaker;

    @Override
    public Mono<List<Country>> getAllCountries() {
        return webClient.get()
                .uri("/all")
                .retrieve()
                .toEntityList(com.assignment.repositories.models.Country.class)
                .doOnError(error -> {
                    throw new TechnicalException(error);
                })
                .transform(items -> circuitBreaker.run(items, Mono::error))
                .map(HttpEntity::getBody)
                .map(items -> items.stream()
                        .map(com.assignment.repositories.models.Country::map)
                        .collect(Collectors.toList())
                );
    }

    @Override
    public Mono<Country> getCountry(String name) {
        return webClient.get()
                .uri("/name/" + name)
                .retrieve()
                .toEntityList(com.assignment.repositories.models.Country.class)
                .doOnError(error -> {
                    throw new TechnicalException(error);
                })
                .transform(item -> circuitBreaker.run(item, Mono::error))
                .map(HttpEntity::getBody)
                .map(items -> {
                    if (items.size() != 1) {
                        throw new NotFoundCountryException(name);
                    }

                    com.assignment.repositories.models.Country item = items.get(0);
                    return item.map();

                });
    }
}
