package com.sylvain.safetyAlerts.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class DataAlreadyExistException extends RuntimeException {

    private static final long serialVersionUID = -6230124407191630387L;

    public DataAlreadyExistException(String message) {
        super(message);
    }

}

