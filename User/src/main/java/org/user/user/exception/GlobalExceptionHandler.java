package org.user.user.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
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

    @ExceptionHandler(AthleteNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponseDTO handleAthleteNotFoundException(AthleteNotFoundException ex, WebRequest request) {
        return createErrorResponse(HttpStatus.NOT_FOUND, "Athlete Not Found", ex.getMessage(), request, null);
    }
}
