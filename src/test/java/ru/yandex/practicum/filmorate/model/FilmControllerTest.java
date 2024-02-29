package ru.yandex.practicum.filmorate.model;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertEquals;

import java.time.LocalDate;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.junit.Before;
import org.junit.Test;

public class FilmControllerTest {
    private Validator validator;

    @Before
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void testShouldReturnFilmSuccess() {
        Film film = new Film();
        film.setName("test");
        film.setDescription("testDesc");
        film.setReleaseDate(LocalDate.now().minusMonths(7));
        film.setDuration(90);
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void testShouldReturnFilmWithFailedName() {
        Film film = new Film();
        film.setName(" ");
        film.setDescription("testDesc");
        film.setReleaseDate(LocalDate.now().minusMonths(7));
        film.setDuration(90);
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertFalse(violations.isEmpty());
        assertEquals(violations.size(), 1);
    }

    @Test
    public void testShouldReturnFilmWithFailedDescription() {
        Film film = new Film();
        film.setName("testFilm");
        film.setDescription("a".repeat(201));
        film.setReleaseDate(LocalDate.now().minusMonths(7));
        film.setDuration(90);
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertFalse(violations.isEmpty());
        assertEquals(violations.size(), 1);
    }

    @Test
    public void testShouldReturnFilmWithFailedDate() {
        Film film = new Film();
        film.setName("test");
        film.setDescription("testDesc");
        film.setReleaseDate(LocalDate.of(1894, 12, 28));
        film.setDuration(90);
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertFalse(violations.isEmpty());
        assertEquals(violations.size(), 1);
    }

    @Test
    public void testShouldReturnFilmWithDuration() {
        Film film = new Film();
        film.setName("test");
        film.setDescription("testDesc");
        film.setReleaseDate(LocalDate.now().minusMonths(7));
        film.setDuration(-20);
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertFalse(violations.isEmpty());
        assertEquals(violations.size(), 1);
    }
}
