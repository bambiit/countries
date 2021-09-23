package com.assignment.services.repositories;

import com.assignment.services.models.Country;
import reactor.core.publisher.Mono;

import java.util.List;

public interface CountryRepository {
    Mono<List<Country>> getAllCountries();
    Mono<Country> getCountry(String name);
}

