package com.assignment.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class NotFoundCountryException extends RuntimeException {
    private final String countryName;
}
