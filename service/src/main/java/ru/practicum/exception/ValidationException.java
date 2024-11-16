package ru.practicum.exception;

import lombok.Getter;

@Getter
public class ValidationException extends RuntimeException {
        private final String reason;

        public ValidationException(String message, String reason) {
            super(message);
            this.reason = reason;
        }
}
