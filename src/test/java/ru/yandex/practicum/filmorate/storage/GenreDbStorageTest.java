package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.db.GenreDbStorage;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@JdbcTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class GenreDbStorageTest {
    private final JdbcTemplate jdbcTemplate;
    private GenreDbStorage genreDbStorage;

    @BeforeEach
    public void setUp() {
        genreDbStorage = new GenreDbStorage(jdbcTemplate);
    }

    @Test
    public void testShouldReturnGetByIdGenre() {
        Genre genre = genreDbStorage.getGenreById(3);
        assertNotNull(genre);
        assertEquals(genre.getName(), "Мультфильм");
    }

    @Test
    public void testShouldReturnFindAllGenres() {
        List<Genre> genres = genreDbStorage.findGenres();
        assertEquals(genres.size(), 6);
        assertEquals(genres.get(2).getName(), "Мультфильм");
    }

}
