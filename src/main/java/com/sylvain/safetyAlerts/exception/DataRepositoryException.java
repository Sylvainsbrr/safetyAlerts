package com.sylvain.safetyAlerts.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class DataRepositoryException extends RuntimeException {
    private static final long serialVersionUID = -245970232718407000L;

    public DataRepositoryException(String message, Throwable cause) {
        super(message, cause);
    }
}

