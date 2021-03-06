package com.assignment.services;

import com.assignment.services.models.Country;
import reactor.core.publisher.Mono;

import java.util.List;

public interface AppServiceInputPort {
    Mono<List<Country>> getAllCountries();

    Mono<Country> getCountry(String name);
}
