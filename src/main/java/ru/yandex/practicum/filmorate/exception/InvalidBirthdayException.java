package ru.yandex.practicum.filmorate.exception;

public class InvalidBirthdayException extends RuntimeException {
    public InvalidBirthdayException() {
        super();
    }

    public InvalidBirthdayException(String message) {
        super(message);
    }

    public InvalidBirthdayException(String message, Throwable cause) {
        super(message, cause);
    }
}
