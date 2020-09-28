package com.example.server.config;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.NoSuchElementException;

@ControllerAdvice
public class ExceptionHandlerAdvice extends ResponseEntityExceptionHandler {

    @ExceptionHandler({MethodArgumentTypeMismatchException.class, NoSuchElementException.class})
    public ResponseEntity<Object> notFoundExceptionHandler(RuntimeException ex) {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler({IllegalArgumentException.class})
    protected ResponseEntity<Object> illegalArgumentExceptionHandler(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<Object> illegalStateExceptionHandler(IllegalStateException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}
