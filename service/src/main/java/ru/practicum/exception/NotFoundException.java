package ru.practicum.exception;

import lombok.Getter;

@Getter
public class NotFoundException extends RuntimeException {
        private final String reason;

        public NotFoundException(String message, String reason) {
            super(message);
            this.reason = reason;
        }
}
