package com.samluiz.ordermgmt.common.exceptions.handler;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.samluiz.ordermgmt.common.exceptions.ResourceNotFoundException;
import com.samluiz.ordermgmt.common.models.StandardError;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;
import java.util.stream.Collectors;

@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<StandardError> resourceNotFound(ResourceNotFoundException e,
                                                          HttpServletRequest request) {
        String error = "Recurso não encontrado.";
        HttpStatus status = HttpStatus.NOT_FOUND;
        StandardError err = new StandardError(
                Instant.now(),
                status.value(),
                error,
                e.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<StandardError> notValid(MethodArgumentNotValidException e,
                                                  HttpServletRequest request) {
        String error = "Dados inválidos.";
        HttpStatus status = HttpStatus.UNPROCESSABLE_ENTITY;
        StandardError err = new StandardError(
                Instant.now(),
                status.value(),
                error,
                e.getBindingResult()
                        .getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .collect(Collectors.toSet())
                        .toString()
                        .replaceAll("\\[*]*", ""),
                request.getRequestURI()
        );
        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(InvalidFormatException.class)
    public ResponseEntity<StandardError> invalidType(InvalidFormatException e,
                                                     HttpServletRequest request) {
        String error = "Dados inválidos. Verifique os tipos de cada campo e tente novamente.";
        HttpStatus status = HttpStatus.UNPROCESSABLE_ENTITY;
        StandardError err = new StandardError(
                Instant.now(),
                status.value(),
                error,
                e.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity.status(status).body(err);
    }
}
