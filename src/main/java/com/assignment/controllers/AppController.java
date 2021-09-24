package com.assignment.controllers;

import com.assignment.controllers.models.Countries;
import com.assignment.controllers.models.Country;
import com.assignment.exceptions.ApplicationError;
import com.assignment.services.AppServiceContract;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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

    @Operation(summary = "Get all countries information")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "A list of countries has been found",
                    content = {
                        @Content(
                                mediaType = MediaType.APPLICATION_JSON_VALUE,
                                schema = @Schema(implementation = Countries.class)
                        )
                    }
            ),
            @ApiResponse(responseCode = "500",
                    description = "Something went wrong!",
                    content = {
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ApplicationError.class)
                            )
                    }
            )
    })
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

    @Operation(summary = "Get specific country details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "A country has been found",
                    content = {
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Country.class)
                            )
                    }
            ),
            @ApiResponse(responseCode = "404",
                    description = "No country has been found to match the query",
                    content = @Content
            ),
            @ApiResponse(responseCode = "500",
                    description = "Something went wrong!",
                    content = {
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ApplicationError.class)
                            )
                    }
            )
    })
    @GetMapping(path = "/{name}", produces = "application/json")
    public Mono<Country> getCountry(@PathVariable String name) {
        return appService.getCountry(name).map(Country::from);
    }
}
