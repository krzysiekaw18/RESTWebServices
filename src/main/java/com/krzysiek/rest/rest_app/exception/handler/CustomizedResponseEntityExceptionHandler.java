package com.krzysiek.rest.rest_app.exception.handler;

import com.krzysiek.rest.rest_app.exception.ElementNotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;

@ControllerAdvice
@RestController
public class CustomizedResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<ExceptionResponse> handleAllException(Exception ex, WebRequest request) {
        return createExceptionEntity(ex, request, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ElementNotFoundException.class)
    public final ResponseEntity<ExceptionResponse> handleElementNotFoundException(Exception ex, WebRequest request) {
        return createExceptionEntity(ex, request, HttpStatus.NOT_FOUND);
    }

    @Override
    protected ResponseEntity handleMethodArgumentNotValid(MethodArgumentNotValidException validException, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return new ResponseEntity<>(createExceptionResponseValidation(validException), HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity<ExceptionResponse> createExceptionEntity(Exception ex, WebRequest request, HttpStatus httpStatus) {
        ExceptionResponse exceptionResponse = createExceptionResponse(ex, request);
        return new ResponseEntity<>(exceptionResponse, httpStatus);
    }

    private ExceptionResponse createExceptionResponse(Exception ex, WebRequest request) {
        return new ExceptionResponse(new Date(), ex.getMessage(), request.getDescription(false));
    }

    private ExceptionResponse createExceptionResponseValidation(MethodArgumentNotValidException methodNotValidException) {
        return new ExceptionResponse(new Date(), "Validation Failed", methodNotValidException.getBindingResult().toString());
    }
}
