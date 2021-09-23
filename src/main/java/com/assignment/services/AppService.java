package com.assignment.services;

import com.assignment.services.models.Country;
import com.assignment.services.repositories.CountryRepository;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

import java.util.List;

@AllArgsConstructor
public class AppService implements AppServiceContract {
    private final CountryRepository repository;

    @Override
    public Mono<List<Country>> getAllCountries() {
        return repository.getAllCountries();
    }

    @Override
    public Mono<Country> getCountry(String name) {
        return repository.getCountry(name);
    }
}
