package ru.practicum.exception;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class ErrorHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)//400
    @ExceptionHandler(value = {ValidationException.class, MethodArgumentNotValidException.class,
            NumberFormatException.class, DataIntegrityViolationException.class, CustomValidationException.class})
    public ApiError handleValidation(final Exception exception) {
        return ApiError.builder()
                .message(exception.getMessage())
                .status(HttpStatus.BAD_REQUEST.toString())
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)//404
    @ExceptionHandler(NotFoundException.class)
    public ApiError handleNotFound(final NotFoundException exception) {
        return ApiError.builder()
                .message(exception.getMessage())
                .reason(exception.getReason())
                .status(HttpStatus.NOT_FOUND.toString())
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ResponseStatus(HttpStatus.CONFLICT)//409
    @ExceptionHandler(value = {ConflictException.class, ConstraintViolationException.class})
    public ApiError handleConflict(final ConflictException exception) {
        return ApiError.builder()
                .message(exception.getMessage())
                .status(HttpStatus.CONFLICT.toString())
                .timestamp(LocalDateTime.now())
                .build();
    }

}
