package com.parking.system.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    private ResponseEntity<Map<String, String>> buildResponse(String message, HttpStatus status) {
        Map<String, String> body = new HashMap<>();
        body.put("message", message);
        return new ResponseEntity<>(body, status);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleEntityNotFoundException(EntityNotFoundException ex) {
        return buildResponse(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(PaymentException.class)
    public ResponseEntity<Map<String, String>> handlePaymentException(PaymentException ex) {
        return buildResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ParkingException.class)
    public ResponseEntity<Map<String, String>> handleParkingException(ParkingException ex) {
        return buildResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<Map<String, String>> handleValidationException(ValidationException ex) {
        return buildResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put("message", fieldName + ": " + errorMessage);
        });
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<Map<String, String>> handleNoResourceFoundException(NoResourceFoundException ex) {
        Map<String, String> body = new HashMap<>();
        body.put("error", "Resource Not Found");
        body.put("message", ex.getMessage());
        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleGlobalException(Exception ex) {
        return buildResponse("An internal server error occurred: " + ex.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
