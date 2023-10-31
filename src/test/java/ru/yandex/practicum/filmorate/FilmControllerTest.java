package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exception.IdNotFoundException;
import ru.yandex.practicum.filmorate.exception.InvalidDescriptionException;
import ru.yandex.practicum.filmorate.exception.InvalidNameException;
import ru.yandex.practicum.filmorate.exception.ReleaseDateException;
import ru.yandex.practicum.filmorate.model.Film;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import java.util.List;

public class FilmControllerTest {
    FilmController filmController = new FilmController();

    @Test
    public void shouldReturnFilmWhenCreate() {
        Film film = new Film("Пираты", "Крутой фильм", LocalDate.of(2003, 5, 10),
                190);

        filmController.create(film);
        List<Film> films = filmController.findAll();
        assertEquals(1, films.size());
        assertEquals("[" + film + "]", filmController.findAll().toString());
    }

    @Test
    public void shouldReturnInvalidNameExceptionWhenCreate() {
        Film film = new Film("", "Крутой фильм", LocalDate.of(2003, 5, 10),
                190);

        InvalidNameException exception = assertThrows(
                InvalidNameException.class,
                () -> filmController.create(film)
        );
        assertEquals("Имя не может быть пустым", exception.getMessage());
    }

    @Test
    public void shouldReturnInvalidDescriptionExceptionWhenCreate() {
        Film film = new Film("Пираты",
                "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
                        "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
                        "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
                        "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
                        "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
                        "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
                        "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
                        "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
                        "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
                        "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
                        "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
                        "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
                        "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
                        "aaaaaaaaaaaaaaaaaa" +
                        "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
                        "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
                        "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
                        "aaaaaaaaaa" +
                        "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
                        "aaaaaaaaaaaaaa" +
                        "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
                        "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa",
                LocalDate.of(2003, 5, 10),
                190);

        InvalidDescriptionException exception = assertThrows(
                InvalidDescriptionException.class,
                () -> filmController.create(film)
        );
        assertEquals("Описание фильма не может быть больше чем 200 символов", exception.getMessage());
    }

    @Test
    public void shouldReturnReleaseDateExceptionWhenCreate() {
        Film film = new Film("Пираты", "Крутой фильм", LocalDate.of(1888, 5, 10),
                190);

        ReleaseDateException exception = assertThrows(
                ReleaseDateException.class,
                () -> filmController.create(film)
        );
        assertEquals("Дата релиза фильма не может быть раньше 1895-12-28", exception.getMessage());
    }

    @Test
    public void shouldReturnInvalidNameExceptionWhenCreateWithEmptyFilm() {
        Film film = new Film();
        InvalidNameException exception = assertThrows(
                InvalidNameException.class,
                () -> filmController.create(film)
        );
        assertEquals("Имя не может быть пустым", exception.getMessage());
    }

    @Test
    public void shouldReturnUpdateFilmWhenUpdate() {
        Film film = new Film("Пираты", "Крутой фильм", LocalDate.of(2003, 5, 10),
                190);
        filmController.create(film);

        Film newFilm = new Film(1, "Пираты 2", "Очень крутой фильм",
                LocalDate.of(2003, 5, 10), 210);
        filmController.update(newFilm);
        assertEquals("[" + newFilm + "]", filmController.findAll().toString());
    }

    @Test
    public void shouldReturnIdNotFoundExceptionWhenUpdate() {
        Film film = new Film("Пираты", "Крутой фильм", LocalDate.of(2003, 5, 10),
                190);
        filmController.create(film);

        Film newFilm = new Film(123, "Пираты 2", "Очень крутой фильм",
                LocalDate.of(2003, 5, 10), 210);

        IdNotFoundException exception = assertThrows(
                IdNotFoundException.class,
                () -> filmController.update(newFilm)
        );
        assertEquals("Фильм не найден", exception.getMessage());
    }
}
