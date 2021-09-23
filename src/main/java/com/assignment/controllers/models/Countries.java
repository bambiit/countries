package com.assignment.controllers.models;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor(staticName = "of")
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class Countries {
    private final List<Country> countries;
}
