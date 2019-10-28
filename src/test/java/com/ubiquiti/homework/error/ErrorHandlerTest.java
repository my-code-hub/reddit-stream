package com.ubiquiti.homework.error;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import static com.ubiquiti.homework.error.ErrorHandler.BAD_REQUEST_MESSAGE;
import static com.ubiquiti.homework.error.ErrorHandler.SERVER_ERROR_MESSAGE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@ExtendWith(MockitoExtension.class)
class ErrorHandlerTest {

    @InjectMocks private ErrorHandler handler;

    @Test
    void unhandled() {
        Exception exception = mock(Exception.class);

        ResponseEntity<ErrorResponse> actual = handler.unhandled(exception);

        assertThat(actual).isEqualTo(response(INTERNAL_SERVER_ERROR, SERVER_ERROR_MESSAGE));
    }

    @Test
    void testUnhandled() {
        MethodArgumentTypeMismatchException exception = mock(MethodArgumentTypeMismatchException.class);

        ResponseEntity<ErrorResponse> actual = handler.unhandled(exception);

        assertThat(actual).isEqualTo(response(BAD_REQUEST, BAD_REQUEST_MESSAGE));
    }

    private ResponseEntity<ErrorResponse> response(HttpStatus status, String message) {
        return ResponseEntity
                .status(status)
                .body(new ErrorResponse(message));
    }
}
