package com.sylvain.safetyAlerts.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class EndpointNotFoundException extends RuntimeException {

    private static final long serialVersionUID = -1453680984345025472L;

    public EndpointNotFoundException(String message) {
        super(message);
    }
}

