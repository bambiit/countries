package com.assignment.exceptions;

import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestControllerAdvice
public class ApplicationExceptionHandler {

    @ExceptionHandler(NotFoundCountryException.class)
    private ResponseEntity handleNotFoundCountryException(NotFoundCountryException exception) {
        log.error("Country {} has not been found", exception.getCountryName());
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(value = {TechnicalException.class, CallNotPermittedException.class})
    private ResponseEntity<ApplicationError> handleTechnicalException(Exception exception) {
        log.error(exception.getMessage(), exception);
        return ResponseEntity.internalServerError()
                .body(ApplicationError.builder()
                        .errorMessage("Something went wrong!")
                        .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .build());
    }

}
