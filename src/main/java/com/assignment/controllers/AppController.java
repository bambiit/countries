package com.assignment.controllers;

import com.assignment.controllers.models.Countries;
import com.assignment.controllers.models.Country;
import com.assignment.services.AppServiceContract;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
@RequestMapping("api/countries")
public class AppController {

    private final AppServiceContract appService;

    @GetMapping(path = "/", produces = "application/json")
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

    @GetMapping(path = "/{name}", produces = "application/json")
    public Mono<Country> getCountry(@PathVariable String name) {
        return appService.getCountry(name).map(Country::from);
    }
}
