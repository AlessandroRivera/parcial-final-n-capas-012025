package com.uca.parcialfinalncapas.exceptions;

import com.uca.parcialfinalncapas.dto.response.ErrorResponse;
import com.uca.parcialfinalncapas.utils.ResponseBuilderUtil;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleEntityNotFoundException(EntityNotFoundException e) {
        return ResponseBuilderUtil.buildErrorResponse(e, HttpStatus.NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(BadTicketRequestException.class)
    public ResponseEntity<ErrorResponse> handleBadTicketRequestException(BadTicketRequestException e) {
        return ResponseBuilderUtil.buildErrorResponse(e, HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFoundException(UserNotFoundException e) {
        return ResponseBuilderUtil.buildErrorResponse(e, HttpStatus.NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(TicketNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleTicketNotFoundException(TicketNotFoundException e) {
        return ResponseBuilderUtil.buildErrorResponse(e, HttpStatus.NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValueOfEntity(MethodArgumentNotValidException e) {
        List<String> errors = e.getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .toList();
        return ResponseBuilderUtil.buildErrorResponse(e, HttpStatus.BAD_REQUEST, errors);
    }
}
