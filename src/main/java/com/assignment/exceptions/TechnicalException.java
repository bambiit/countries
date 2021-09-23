package com.assignment.exceptions;

public class TechnicalException extends RuntimeException {
    public TechnicalException(Throwable error) {
        super(error);
    }
}
