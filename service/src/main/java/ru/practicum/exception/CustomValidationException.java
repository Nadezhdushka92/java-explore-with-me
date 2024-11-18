package ru.practicum.exception;

import org.springframework.web.bind.MethodArgumentNotValidException;

public class CustomValidationException extends RuntimeException {
    public CustomValidationException(String message) {
        super(message);
    }

    public CustomValidationException(MethodArgumentNotValidException ex) {
        super("Validation failed: " + ex.getBindingResult().getFieldErrors());
    }
}
