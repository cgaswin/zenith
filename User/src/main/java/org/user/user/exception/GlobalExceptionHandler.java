package org.user.user.exception;

import org.springframework.dao.DataIntegrityViolationException;
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
import java.util.Objects;

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

    @ExceptionHandler(CoachNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponseDTO handleCoachNotFoundException(CoachNotFoundException ex, WebRequest request) {
        return createErrorResponse(HttpStatus.NOT_FOUND, "Coach Not Found", ex.getMessage(), request, null);
    }

    @ExceptionHandler(CoachingRelationshipNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponseDTO handleCoachingRelationshipNotFoundException(CoachingRelationshipNotFoundException ex, WebRequest request) {
        return createErrorResponse(HttpStatus.NOT_FOUND, "Coaching Relationship Not Found", ex.getMessage(), request, null);
    }

    @ExceptionHandler(CoachingRequestNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponseDTO handleCoachingRequestNotFoundException(CoachingRequestNotFoundException ex, WebRequest request) {
        return createErrorResponse(HttpStatus.NOT_FOUND, "Coaching Request Not Found", ex.getMessage(), request, null);
    }

    @ExceptionHandler(CoachAlreadyAssignedException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponseDTO handleCoachAlreadyAssignedException(CoachAlreadyAssignedException ex, WebRequest request) {
        return createErrorResponse(HttpStatus.NOT_FOUND, "Coach Already Assigned ", ex.getMessage(), request, null);
    }

    @ExceptionHandler(CoachNotAvailableForRequestException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponseDTO handleCoachNotAvailableForRequestException(CoachNotAvailableForRequestException ex, WebRequest request) {
        return createErrorResponse(HttpStatus.NOT_FOUND, "Coach is not available to accept request", ex.getMessage(), request, null);
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

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponseDTO handleDataIntegrityViolationException(DataIntegrityViolationException ex, WebRequest request) {
        String message = "Unique constraint violation: " + Objects.requireNonNull(ex.getRootCause()).getMessage();
        return createErrorResponse(HttpStatus.BAD_REQUEST, "Data Integrity Violation", message, request, null);
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
