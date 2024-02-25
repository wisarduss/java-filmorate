package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.exception.DataNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.db.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.db.GenreDbStorage;
import ru.yandex.practicum.filmorate.storage.db.MpaDbStorage;
import ru.yandex.practicum.filmorate.util.DatabaseUtil;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

@JdbcTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FilmDbStorageTest {
    private final JdbcTemplate jdbcTemplate;
    private FilmDbStorage filmDbStorage;
    private Mpa mpa1;
    private Mpa mpa2;
    private Genre genre1;
    private Genre genre2;

    @BeforeEach
    public void setUp() {
        DatabaseUtil databaseUtil = new DatabaseUtil(jdbcTemplate);
        filmDbStorage = new FilmDbStorage(jdbcTemplate, databaseUtil);

        MpaDbStorage mpaDbStorage = new MpaDbStorage(jdbcTemplate);
        mpa1 = mpaDbStorage.getMpaById(1);
        mpa2 = mpaDbStorage.getMpaById(2);

        GenreDbStorage genreDbStorage = new GenreDbStorage(jdbcTemplate);
        genre1 = genreDbStorage.getGenreById(1);
        genre2 = genreDbStorage.getGenreById(2);
    }

    @Test
    public void testShouldReturnCreateFilm() {
        Film film = new Film();
        film.setName("test");
        film.setDescription("testDesc");
        film.setDuration(90);
        film.setReleaseDate(LocalDate.of(2015, 3, 12));
        film.setMpa(mpa1);
        film.setGenres(new HashSet<>());
        filmDbStorage.create(film);

        Film savedFilm = filmDbStorage.getById(6);
        assertEquals(savedFilm.getName(), film.getName());
        assertEquals(savedFilm.getDescription(), film.getDescription());
        assertEquals(savedFilm.getDuration(), film.getDuration());
        assertEquals(savedFilm.getReleaseDate(), film.getReleaseDate());
        assertEquals(savedFilm.getMpa(), film.getMpa());
        assertEquals(savedFilm.getGenres(), film.getGenres());
    }

    @Test
    public void testShouldReturnUpdateFilm() {
        Film film = new Film();
        film.setName("test");
        film.setDescription("testDesc");
        film.setDuration(90);
        film.setReleaseDate(LocalDate.of(2015, 3, 12));
        film.setMpa(mpa1);
        film.setGenres(new HashSet<>(Arrays.asList(genre1, genre2)));
        Film savedFilm = filmDbStorage.create(film);

        savedFilm.setGenres(new HashSet<>(Collections.singletonList(genre1)));
        savedFilm.setMpa(mpa2);
        savedFilm.setDuration(5);
        filmDbStorage.update(savedFilm);
        Film savedFilmAfterChange = filmDbStorage.getById(8);

        assertEquals(savedFilmAfterChange.getGenres(), new HashSet<>(Collections.singletonList(genre1)));
        assertEquals(savedFilmAfterChange.getMpa(), mpa2);
        assertEquals(savedFilmAfterChange.getDuration(), 5);

        Film film2 = new Film();
        film2.setName("test");
        film2.setDescription("testDesc");
        film2.setDuration(90);
        film2.setReleaseDate(LocalDate.of(2015, 3, 12));
        film2.setMpa(mpa1);
        film2.setGenres(new HashSet<>(Arrays.asList(genre1, genre2)));

        assertThrows(DataNotFoundException.class, () -> {
            filmDbStorage.update(film2);
        });
    }

    @Test
    public void testShouldReturnDeleteFilm() {
        Film film = new Film();
        film.setName("test");
        film.setDescription("testDesc");
        film.setDuration(90);
        film.setReleaseDate(LocalDate.of(2015, 3, 12));
        film.setMpa(mpa1);
        film.setGenres(new HashSet<>());
        filmDbStorage.create(film);

        Film savedFilm = filmDbStorage.getById(7);
        filmDbStorage.delete(savedFilm.getId());

        assertThrows(DataNotFoundException.class, () -> {
            filmDbStorage.getById(7);
        });
        assertThrows(DataNotFoundException.class, () -> {
            filmDbStorage.delete(2L);
        });
    }

    @Test
    public void testShouldReturnGetByIdFilm() {
        Film film = new Film();
        film.setName("test");
        film.setDescription("testDesc");
        film.setDuration(90);
        film.setReleaseDate(LocalDate.of(2015, 3, 12));
        film.setMpa(mpa1);
        film.setGenres(new HashSet<>());
        filmDbStorage.create(film);

        Film savedFilm = filmDbStorage.getById(9);

        assertEquals(savedFilm.getName(), film.getName());
        assertEquals(savedFilm.getDescription(), film.getDescription());

    }

    @Test
    public void testShouldReturnFindAllFilm() {
        Film film = new Film();
        film.setName("test");
        film.setDescription("testDesc");
        film.setDuration(90);
        film.setReleaseDate(LocalDate.of(2015, 3, 12));
        film.setMpa(mpa1);
        film.setGenres(new HashSet<>());
        filmDbStorage.create(film);

        Film film2 = new Film();
        film2.setName("test");
        film2.setDescription("testDesc");
        film2.setDuration(1);
        film2.setReleaseDate(LocalDate.of(2015, 3, 12));
        film2.setMpa(mpa1);
        film2.setGenres(new HashSet<>(Arrays.asList(genre1, genre2)));
        filmDbStorage.create(film2);

        assertEquals(filmDbStorage.findFilms().size(), 2);
    }
}
