package ru.yandex.practicum.filmorate.exception;

public class InvalidDescriptionException extends RuntimeException {
    public InvalidDescriptionException() {
        super();
    }

    public InvalidDescriptionException(String message) {
        super(message);
    }

    public InvalidDescriptionException(String message, Throwable cause) {
        super(message, cause);
    }
}
