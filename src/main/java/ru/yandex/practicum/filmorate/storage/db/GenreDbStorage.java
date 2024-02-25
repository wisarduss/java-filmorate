package ru.yandex.practicum.filmorate.storage.db;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.DataNotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;

import java.util.Collections;
import java.util.List;

@Component
@RequiredArgsConstructor
public class GenreDbStorage implements GenreStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Genre> findGenres() {
        String sqlQuery = "SELECT id, name" +
                " FROM genres;";
        return jdbcTemplate.query(sqlQuery, getGenreMapper);
    }

    @Override
    public Genre getGenreById(long id) {
        String sqlQuery = "SELECT id, name" +
                " FROM genres " +
                "WHERE id = ?;";
        try {
            return jdbcTemplate.queryForObject(sqlQuery, getGenreMapper, id);
        } catch (EmptyResultDataAccessException e) {
            throw new DataNotFoundException("Жанр с id =  " + id + " не найден", HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public List<Genre> getByIds(List<Long> genreIds) {
        if (genreIds == null || genreIds.isEmpty()) {
            return Collections.emptyList();
        }

        String inSqlQuery = String.join(",", Collections.nCopies(genreIds.size(), "?"));
        String sqlQuery = "SELECT id, name " +
                "FROM genres " +
                "WHERE id IN (" + inSqlQuery + ");";

        return jdbcTemplate.query(sqlQuery, getGenreMapper, genreIds.toArray());
    }

    private final RowMapper<Genre> getGenreMapper = (resultSet, rowNum) -> {
        Genre genre = new Genre();
        genre.setId(resultSet.getLong("id"));
        genre.setName(resultSet.getString("name"));
        return genre;
    };

}
