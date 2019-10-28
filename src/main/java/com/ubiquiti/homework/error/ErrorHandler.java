package com.ubiquiti.homework.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import lombok.extern.slf4j.Slf4j;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {

    public static final String BAD_REQUEST_MESSAGE = "Bad request. Make sure your request body and " +
            "parameter(s) are valid.";

    static final String SERVER_ERROR_MESSAGE = "Oops, something went terribly wrong.";

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> unhandled(MethodArgumentTypeMismatchException e) {
        log.debug("incorrect method arguments", e);
        return response(BAD_REQUEST, BAD_REQUEST_MESSAGE);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> unhandled(Exception e) {
        log.error("unhandled exception", e);
        return response(INTERNAL_SERVER_ERROR, SERVER_ERROR_MESSAGE);
    }

    private ResponseEntity<ErrorResponse> response(HttpStatus status, String message) {
        return ResponseEntity
                .status(status)
                .body(new ErrorResponse(message));
    }
}
