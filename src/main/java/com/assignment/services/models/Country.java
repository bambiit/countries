package com.assignment.services.models;

import lombok.Data;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Data
public class Country {
    private String name;
    private String countryCode;
    private String capital;
    private Integer population;
    private String flagFileUrl;
}
