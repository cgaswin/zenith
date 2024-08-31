package org.event.event.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private ErrorResponseDTO createErrorResponse(HttpStatus status, String error, String message, WebRequest request, Map<String, String> errors) {
        return new ErrorResponseDTO(
                LocalDateTime.now(),
                status.value(),
                error,
                message,
                request.getDescription(false),
                errors
        );
    }

    @ExceptionHandler(EventNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponseDTO handleEventNotFoundException(EventNotFoundException ex, WebRequest request) {
        return createErrorResponse(HttpStatus.NOT_FOUND, "Event Not Found", ex.getMessage(), request, null);
    }

    @ExceptionHandler(EventItemNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponseDTO handleEventItemNotFoundException(EventItemNotFoundException ex, WebRequest request) {
        return createErrorResponse(HttpStatus.NOT_FOUND, "Event Item Not Found", ex.getMessage(), request, null);
    }

    @ExceptionHandler(EventRegistrationNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponseDTO handleEventRegistrationNotFoundException(EventRegistrationNotFoundException ex, WebRequest request){
        return createErrorResponse(HttpStatus.NOT_FOUND,"Event Registration Not Found", ex.getMessage(), request,null);
    }

    @ExceptionHandler(ResultNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponseDTO handleResultNotFoundException(ResultNotFoundException ex, WebRequest request){
        return createErrorResponse(HttpStatus.NOT_FOUND,"Result Not Found", ex.getMessage(), request,null);
    }

    @ExceptionHandler(AuthorizationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponseDTO handleAuthorizationException(AuthorizationException ex, WebRequest request){
        return createErrorResponse(HttpStatus.NOT_FOUND,"Authorization error", ex.getMessage(), request,null);
    }

    @ExceptionHandler(S3UploadException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponseDTO handleS3UploadException(S3UploadException ex, WebRequest request){
        return createErrorResponse(HttpStatus.UNAUTHORIZED, "Error uploading to s3", ex.getMessage(), request, null);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponseDTO handleValidationExceptions(MethodArgumentNotValidException ex, WebRequest request) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        return createErrorResponse(HttpStatus.BAD_REQUEST, "Validation Error", "Validation failed", request, errors);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponseDTO handleGlobalException(Exception ex, WebRequest request) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        ex.printStackTrace(pw);
        String stackTrace = sw.toString();

        Map<String, String> errors = new HashMap<>();
        errors.put("stackTrace", stackTrace);
        return createErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Internal Server Error",
                "An unexpected error occurred",
                request,
                errors
        );
    }
}
