package ru.yandex.practicum.filmorate.exception;

public class ReleaseDateException extends RuntimeException {
    public ReleaseDateException() {
        super();
    }

    public ReleaseDateException(String message) {
        super(message);
    }

    public ReleaseDateException(String message, Throwable cause) {
        super(message, cause);
    }
}
