package com.assignment.controllers;

import com.assignment.controllers.models.Countries;
import com.assignment.controllers.models.Country;
import com.assignment.services.AppServiceInputPort;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
public class AppController implements ApiContract {

    private final AppServiceInputPort appService;

    public Mono<Countries> getAllCountries() {
        return appService.getAllCountries()
                .map(
                        items -> Countries.of(
                                items.stream()
                                        .map(Country::from)
                                        .collect(Collectors.toList())
                        )
                );
    }

    public Mono<Country> getCountry(@PathVariable String name) {
        return appService.getCountry(name).map(Country::from);
    }
}
