package com.assignment.controllers.models;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class Country extends com.assignment.services.models.Country {

    public static Country from(com.assignment.services.models.Country country) {
        return Country.builder()
                .name(country.getName())
                .countryCode(country.getCountryCode())
                .capital(country.getCapital())
                .flagFileUrl(country.getFlagFileUrl())
                .population(country.getPopulation())
                .build();
    }
}
