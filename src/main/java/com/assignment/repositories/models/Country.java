package com.assignment.repositories.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class Country {
    private String name;

    @JsonProperty("alpha2Code")
    private String countryCode;

    private String capital;

    private Integer population;

    @JsonProperty("flag")
    private String flagFileUrl;

    public com.assignment.services.models.Country map() {
        return com.assignment.services.models.Country
                .builder()
                .name(this.name)
                .capital(this.capital)
                .countryCode(this.countryCode)
                .population(this.population)
                .flagFileUrl(this.flagFileUrl)
                .build();
    }
}
