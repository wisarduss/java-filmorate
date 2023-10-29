package ru.yandex.practicum.filmorate.exception;

public class InvalidDurationTimeException extends RuntimeException {
    public InvalidDurationTimeException() {
        super();
    }

    public InvalidDurationTimeException(String message) {
        super(message);
    }

    public InvalidDurationTimeException(String message, Throwable cause) {
        super(message, cause);
    }
}
