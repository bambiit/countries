package com.assignment.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class ApplicationExceptionHandler {

    @ExceptionHandler(NotFoundCountryException.class)
    private ResponseEntity handleNotFoundCountryException() {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(TechnicalException.class)
    private ResponseEntity<ApplicationError> handleTechnicalException(TechnicalException exception) {
        log.error(exception.getMessage(), exception);
        return ResponseEntity.internalServerError()
                .body(ApplicationError.builder()
                        .errorMessage("Something went wrong!")
                        .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .build());
    }
}
