package com.krzysiek.rest.rest_app.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class ElementAlreadyExistsOnListException extends RuntimeException {

    public ElementAlreadyExistsOnListException(String message) {
        super(message);
    }
}
