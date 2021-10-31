package com.sylvain.safetyAlerts.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class DataNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 5592555479766493434L;

    public DataNotFoundException(String message) {
        super(message);
    }
}

