package org.tfg.grant_java.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.tfg.grant_java.config.ErrorConfig;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    private final Map<String, ErrorDetails> errorDetailsMap;

    private final ErrorDetails defaultErrorDetails = ErrorDetails.builder()
            .errorCode("9999")
            .message("An unexpected error occurred. Please try again later.")
            .httpStatus(200)
            .errorType("UnknownError")
            .build();

    public GlobalExceptionHandler(ErrorConfig errorConfig) {
        this.errorDetailsMap = errorConfig.getErrorDetails().stream()
                .collect(Collectors.toMap(ErrorDetails::getErrorCode, e -> e));
    }

    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<ErrorDetails> handleServiceException(ServiceException ex) {

        ErrorDetails errorDetails =
                errorDetailsMap.getOrDefault(ex.getErrorCode(), defaultErrorDetails);

        // Business exception → WARN (no stack trace)
        log.warn(
                "ServiceException handled | errorCode={} | errorType={} | httpStatus={} | message={}",
                errorDetails.getErrorCode(),
                errorDetails.getErrorType(),
                errorDetails.getHttpStatus(),
                errorDetails.getMessage()
        );

        return ResponseEntity
                .status(errorDetails.getHttpStatus())
                .body(errorDetails);
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationErrors(MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors()
                .forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));

        // Validation failure → WARN
        log.warn(
                "Validation failed | errors={}",
                errors
        );

        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorDetails> handleUsernameException(Exception ex) {

        // Unexpected exception → ERROR + stack trace
        log.error(
                "Unhandled exception occurred",
                ex
        );

        return ResponseEntity
                .status(200)
                .body(ErrorDetails.builder()
                        .errorCode("9999")
                        .message("Invalid Credentials")
                        .httpStatus(200)
                        .errorType("UnknownError")
                        .build());
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDetails> handleUnhandledException(Exception ex) {

        // Unexpected exception → ERROR + stack trace
        log.error(
                "Unhandled exception occurred",
                ex
        );

        return ResponseEntity
                .status(defaultErrorDetails.getHttpStatus())
                .body(defaultErrorDetails);
    }

}