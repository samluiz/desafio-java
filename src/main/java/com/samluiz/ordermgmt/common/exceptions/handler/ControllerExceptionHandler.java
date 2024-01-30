package com.samluiz.ordermgmt.common.exceptions.handler;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.samluiz.ordermgmt.auth.user.exceptions.UsernameEmUsoException;
import com.samluiz.ordermgmt.auth.user.exceptions.UsernameNaoEncontradoException;
import com.samluiz.ordermgmt.common.exceptions.PedidoException;
import com.samluiz.ordermgmt.common.exceptions.ProdutoException;
import com.samluiz.ordermgmt.common.exceptions.RecursoNaoEncontradoException;
import com.samluiz.ordermgmt.common.models.StandardError;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(RecursoNaoEncontradoException.class)
    public ResponseEntity<StandardError> resourceNotFound(RecursoNaoEncontradoException e,
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

    @ExceptionHandler(ProdutoException.class)
    public ResponseEntity<StandardError> produtoException(ProdutoException e,
                                                          HttpServletRequest request) {
        String error = "Erro ao fazer operação com produto.";
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        StandardError err = new StandardError(
                Instant.now(),
                status.value(),
                error,
                e.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(PedidoException.class)
    public ResponseEntity<StandardError> pedidoException(PedidoException e,
                                                         HttpServletRequest request) {
        String error = "Erro ao fazer operação com pedido.";
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        StandardError err = new StandardError(
                Instant.now(),
                status.value(),
                error,
                e.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(UsernameEmUsoException.class)
    public ResponseEntity<StandardError> usernameEmUso(UsernameEmUsoException e,
                                                       HttpServletRequest request) {
        String error = "Username já está em uso.";
        HttpStatus status = HttpStatus.CONFLICT;
        StandardError err = new StandardError(
                Instant.now(),
                status.value(),
                error,
                e.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(UsernameNaoEncontradoException.class)
    public ResponseEntity<StandardError> usernameNaoEncontrado(UsernameNaoEncontradoException e,
                                                               HttpServletRequest request) {
        String error = "Username não encontrado.";
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

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<StandardError> invalidId(MethodArgumentTypeMismatchException e,
                                                   HttpServletRequest request) {
        String error = "UUID inválido.";
        HttpStatus status = HttpStatus.BAD_REQUEST;
        StandardError err = new StandardError(
                Instant.now(),
                status.value(),
                error,
                e.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<StandardError> illegalArgument(IllegalArgumentException e,
                                                         HttpServletRequest request) {
        String error = "Dados inválidos.";
        HttpStatus status = HttpStatus.BAD_REQUEST;
        StandardError err = new StandardError(
                Instant.now(),
                status.value(),
                error,
                e.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<StandardError> invalidBody(HttpMessageNotReadableException e,
                                                     HttpServletRequest request) {
        String error = "Corpo da requisição inválido ou mal formatado.";
        HttpStatus status = HttpStatus.BAD_REQUEST;
        StandardError err = new StandardError(
                Instant.now(),
                status.value(),
                error,
                e.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<StandardError> illegalState(IllegalStateException e,
                                                      HttpServletRequest request) {
        String error = "Estado inválido..";
        HttpStatus status = HttpStatus.BAD_REQUEST;
        StandardError err = new StandardError(
                Instant.now(),
                status.value(),
                error,
                e.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<StandardError> dataIntegrityError(DataIntegrityViolationException e,
                                                            HttpServletRequest request) {
        String error = "Erro de integridade de dados.";
        HttpStatus status = HttpStatus.BAD_REQUEST;
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
