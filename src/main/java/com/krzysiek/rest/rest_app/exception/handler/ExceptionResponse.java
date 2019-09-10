package com.krzysiek.rest.rest_app.exception.handler;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.Date;

@Data
public class ExceptionResponse {

    private Date timestamp;
    private String message;
    private String details;

    public ExceptionResponse(Date timestamp, String message, String details) {
        this.timestamp = timestamp;
        this.message = message;
        this.details = details;
    }
}
