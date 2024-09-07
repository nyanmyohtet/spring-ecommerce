package com.nyanmyohtet.ecommerceservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.util.ConcurrentModificationException;
import java.util.Date;

@ControllerAdvice
public class GlobalExceptionHandlerController {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Object> resourceNotFound(ResourceNotFoundException ex,
                                                   HttpServletRequest request) {
        Error error = new Error();
        error.setMessage(ex.getMessage());
        error.setTimestamp(new Date().getTime());
        error.setStatus(HttpStatus.NOT_FOUND.value());
        return new ResponseEntity<>(error, null, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> methodArgumentNotValid(MethodArgumentNotValidException ex, HttpServletRequest request) {
        Error error = new Error();
        error.setMessage(ex.getMessage());
        error.setTimestamp(new Date().getTime());
        error.setStatus(HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(error, null, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<Object> badRequest(BadRequestException ex, HttpServletRequest request) {
        Error error = new Error();
        error.setMessage(ex.getMessage());
        error.setTimestamp(new Date().getTime());
        error.setStatus(HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(error, null, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Object> internalServerError(RuntimeException ex, HttpServletRequest request) {
        Error error = new Error();
        error.setMessage(ex.getMessage());
        error.setTimestamp(new Date().getTime());
        error.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        return new ResponseEntity<>(error, null, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ConcurrentModificationException.class)
    public ResponseEntity<Object> conflictRequest(ConcurrentModificationException ex, HttpServletRequest request) {
        Error error = new Error();
        error.setMessage(ex.getMessage());
        error.setTimestamp(new Date().getTime());
        error.setStatus(HttpStatus.CONFLICT.value());
        return new ResponseEntity<>(error, null, HttpStatus.CONFLICT);
    }
}
