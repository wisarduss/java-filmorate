package ru.yandex.practicum.filmorate.exception;

public class FilmAndUserValidationException extends RuntimeException {
    public FilmAndUserValidationException() {
        super();
    }

    public FilmAndUserValidationException(String message) {
        super(message);
    }

    public FilmAndUserValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
