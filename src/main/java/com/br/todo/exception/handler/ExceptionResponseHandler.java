package com.br.todo.exception.handler;

import com.auth0.jwt.exceptions.JWTDecodeException;
import com.br.todo.exception.InvalidTokenException;
import com.br.todo.exception.RegraDeNegocioException;
import com.br.todo.exception.ResourceNotFoundException;
import com.br.todo.exception.ValidationException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.internal.engine.path.PathImpl;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.*;

@Slf4j
@RestControllerAdvice
public class ExceptionResponseHandler {

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorHandlerResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        log.error("HttpMessageNotReadableException occurred: {}", ex.getMessage());
        ErrorHandlerResponse errorResponse = ErrorHandlerResponse.builder().status(HttpStatus.BAD_REQUEST.value()).
                error("MESSAGE_NOT_READABLE").
                message("Corpo da solicitação com JSON inválido.").
                timestamp(System.currentTimeMillis()).build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorHandlerResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        log.error("MethodArgumentNotValidException occurred: {}", ex.getMessage());
        Map<String, List<String>> fieldErrors = new HashMap<>();

        ex.getBindingResult().getAllErrors().forEach(error -> {
            fieldErrors.computeIfAbsent(((FieldError) error).getField(),
                    k -> new ArrayList<>()).add(error.getDefaultMessage());

        });

        ErrorHandlerResponse errorResponse = ErrorHandlerResponse.builder().status(HttpStatus.BAD_REQUEST.value()).
                error("FIELD_VALIDATION_ERROR").
                message("Erro na validação dos dados enviados. Verifique os campos informados.").
                timestamp(System.currentTimeMillis()).
                fieldErrors(fieldErrors).build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorHandlerResponse> handleResourceNotFoundException(ResourceNotFoundException ex) {
        log.error("ResourceNotFoundException occurred: {}", ex.getMessage());
        ErrorHandlerResponse errorResponse = ErrorHandlerResponse.builder().status(HttpStatus.NOT_FOUND.value())
                .error("RESOURCE_NOT_FOUND").message(ex.getMessage()).timestamp(System.currentTimeMillis()).build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(RegraDeNegocioException.class)
    public ResponseEntity<ErrorHandlerResponse> handleRegraDeNegocioException(RegraDeNegocioException ex) {
        log.error("RegraDeNegocioException occurred: {}", ex.getMessage());
        ErrorHandlerResponse errorResponse = ErrorHandlerResponse.builder().status(HttpStatus.BAD_REQUEST.value())
                .error("RegraDeNegocioException").message(ex.getMessage()).timestamp(System.currentTimeMillis()).build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ErrorHandlerResponse> handleUsernameNotFoundException(UsernameNotFoundException ex) {
        log.error("UsernameNotFoundException occurred: {}", ex.getMessage());
        ErrorHandlerResponse errorResponse = ErrorHandlerResponse.builder().status(HttpStatus.UNAUTHORIZED.value())
                .error("UsernameNotFoundException").message(ex.getMessage()).timestamp(System.currentTimeMillis()).build();
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorHandlerResponse> handleConstraintViolationException(ConstraintViolationException ex) {
        log.error("ConstraintViolationException occurred: {}", ex.getMessage());
        ErrorHandlerResponse errorResponse = ErrorHandlerResponse.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .error("VALIDATION_ERROR")
                .message("Erro de validação. Verifique os dados informados.")
                .timestamp(System.currentTimeMillis())
                .build();

        Map<String, List<String>> fieldErrors = new HashMap<>();
        Set<ConstraintViolation<?>> constraintViolations = ex.getConstraintViolations();
        for (ConstraintViolation<?> violation : constraintViolations) {
            String fieldName = ((PathImpl) violation.getPropertyPath()).getLeafNode().getName();
            String errorMessage = violation.getMessage();
            fieldErrors.computeIfAbsent(fieldName, k -> new ArrayList<>()).add(errorMessage);
        }

        errorResponse.setFieldErrors(fieldErrors);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorHandlerResponse> handleValidationException(ValidationException ex) {
        log.warn("ValidationException occurred: {}", ex.getErrors());
        ErrorHandlerResponse errorResponse = ErrorHandlerResponse.builder().status(HttpStatus.BAD_REQUEST.value())
                .error("VALIDATION_ERROR").timestamp(System.currentTimeMillis()).build();

        errorResponse.setFieldErrors(ex.getErrors());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }


    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<ErrorHandlerResponse> handleInvalidTokenException(InvalidTokenException ex) {
        log.error("InvalidTokenException occurred: {}", ex.getMessage());
        ErrorHandlerResponse errorResponse = ErrorHandlerResponse.builder().status(HttpStatus.UNAUTHORIZED.value())
                .error("INVALID_TOKEN").message(ex.getMessage()).timestamp(System.currentTimeMillis()).build();
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }

    @ExceptionHandler(JWTDecodeException.class)
    public ResponseEntity<ErrorHandlerResponse> handleInvalidTokenException(JWTDecodeException ex) {
        log.error("InvalidTokenException occurred: {}", ex.getMessage());
        ErrorHandlerResponse errorResponse = ErrorHandlerResponse.builder().status(HttpStatus.UNAUTHORIZED.value())
                .error("INVALID_TOKEN").message(ex.getMessage()).timestamp(System.currentTimeMillis()).build();
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorHandlerResponse> handleAuthenticationException(AuthenticationException ex) {
        ErrorHandlerResponse errorResponse = ErrorHandlerResponse.builder().status(HttpStatus.UNAUTHORIZED.value())
                .error("UNAUTHORIZED").message("Não foi possível autenticar seu login. Verifique se o nome de usuário e senha " +
                        "estão corretos e tente novamente.").timestamp(System.currentTimeMillis()).build();
        if (ex instanceof BadCredentialsException) {
            log.error("BadCredentialsException occurred: {}", ex.getMessage());
        } else {
            log.error("AuthenticationException occurred: {}", ex.getMessage());
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Object> handleAccessDeniedException(Exception e, WebRequest request) {
        return new ResponseEntity<Object>("Acesso negado!", HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorHandlerResponse> handleBadCredentialsException(BadCredentialsException ex) {
        log.error("BadCredentialsException occurred: {}", ex.getMessage());
        ErrorHandlerResponse errorResponse = ErrorHandlerResponse.builder().status(HttpStatus.UNAUTHORIZED.value())
                .error("UNAUTHORIZED").message("Email ou senha incorreto(s). Verifique suas credenciais e tente novamente.").timestamp(System.currentTimeMillis()).build();
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorHandlerResponse> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        log.error("DataIntegrityViolationException occurred: {}", ex.getMessage());
        ErrorHandlerResponse errorResponse = ErrorHandlerResponse.builder().status(HttpStatus.BAD_REQUEST.value())
                .error("DATA_INTEGRITY_VIOLATION").message(ex.getMessage()).timestamp(System.currentTimeMillis()).build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }
}
