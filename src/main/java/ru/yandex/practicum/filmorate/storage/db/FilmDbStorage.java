package ru.yandex.practicum.filmorate.storage.db;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.DataNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.util.DatabaseUtil;
import ru.yandex.practicum.filmorate.util.statement.FilmPreparedStatementSetter;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.util.*;

@Repository
@RequiredArgsConstructor
public class FilmDbStorage implements FilmStorage {
    private static final int MAX_LENGTH = 200;
    private static final LocalDate MIN_DATE_RELEASE = LocalDate.of(1895, 12, 28);

    private final JdbcTemplate jdbcTemplate;
    private final DatabaseUtil databaseUtil;


    @Override
    public List<Film> findFilms() {
        String sqlQuery = "SELECT" +
                " f.*, " +
                "  m.id as mpa_id, " +
                "  m.name as mpa_name, " +
                "  g.id as genre_id, " +
                "  g.name as genre_name " +
                "FROM films f " +
                "JOIN mpa m ON f.mpa_id = m.id " +
                "LEFT JOIN film_genres fg ON f.id = fg.film_id " +
                "LEFT JOIN genres g ON fg.genre_id = g.id";

        Map<Long, Film> films = new HashMap<>();
        jdbcTemplate.query(sqlQuery, resultSet -> {
            long filmId = resultSet.getInt("id");
            Film film = films.get(filmId);
            if (film == null) {
                film = getFilmMapper.mapRow(resultSet, resultSet.getRow());
                if (film != null) {
                    film.setGenres(new HashSet<>());
                    films.put(filmId, film);
                }

            }
            int genreId = resultSet.getInt("genre_id");
            if (genreId != 0) {
                Genre genre = getGenreMapper.mapRow(resultSet, resultSet.getRow());
                if (film != null) {
                    film.getGenres().add(genre);
                }
            }
        });
        return new ArrayList<>(films.values());
    }

    @Override
    public Film update(Film film) {
        long filmId = film.getId();
        String sqlQuery = "UPDATE films SET name = ?, description = ?, release_date = ?," +
                " duration = ?, mpa_id = ?" +
                " where id = ?";
        int update = jdbcTemplate.update(
                sqlQuery, film.getName(), film.getDescription(), Date.valueOf(film.getReleaseDate()),
                film.getDuration(), film.getMpa().getId(), filmId
        );
        if (update == 0) {
            throw new DataNotFoundException("Фильм с id =  " + filmId + " не найден", HttpStatus.NOT_FOUND);
        }

        String deleteSqlQuery = "DELETE FROM film_genres" +
                " WHERE film_id = ?";
        jdbcTemplate.update(deleteSqlQuery, film.getId());

        if (!film.getGenres().isEmpty()) {
            insertFilmGenres(filmId, film.getGenres());
        }

        return film;
    }

    @Override
    public Film getById(long id) {
        String sqlQuery = "SELECT " +
                "  f.id, " +
                "  f.name," +
                "  f.description," +
                "  f.release_date," +
                "  f.duration," +
                "  m.id as mpa_id, " +
                "  m.name as mpa_name " +
                "FROM films f JOIN mpa m ON f.mpa_id = m.id " +
                "WHERE f.id = ?";
        try {
            Film film = jdbcTemplate.queryForObject(sqlQuery, getFilmMapper, id);
            if (film != null) {
                Set<Genre> genres = getGenresByFilm(film.getId());
                film.setGenres(genres);
            }
            return film;
        } catch (EmptyResultDataAccessException e) {
            throw new DataNotFoundException("Фильм с id = " + id + " не найден",HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public Film create(Film film) {
        String sqlQuery = "INSERT INTO films (name, description, release_date, duration, mpa_id)" +
                " VALUES (?, ?, ?, ?, ?);";
        int filmId = databaseUtil.insertAndReturnId(sqlQuery, new FilmPreparedStatementSetter(film));
        film.setId(filmId);

        if (!film.getGenres().isEmpty()) {
            insertFilmGenres(filmId, film.getGenres());
        }

        return film;
    }

    @Override
    public void delete(Long id) {
        String deleteGenreSqlQuery = "DELETE FROM film_genres WHERE film_id = ?";
        jdbcTemplate.update(deleteGenreSqlQuery, id);

        String deleteFilmSqlQuery = "DELETE FROM films where id = ?";
        int update = jdbcTemplate.update(deleteFilmSqlQuery, id);
        if (update == 0) {
            throw new DataNotFoundException("Фильм с id =  " + id + " не найден");
        }
    }

    public Set<Genre> getGenresByFilm(Long id) {
        String sqlQuery = "SELECT g.id as genre_id, g.name as genre_name FROM genres g" +
                " JOIN film_genres fg ON g.id = fg.genre_id WHERE fg.film_id = ?;";
        return new HashSet<>(jdbcTemplate.query(sqlQuery, getGenreMapper, id));
    }

    private final RowMapper<Film> getFilmMapper = (resultSet, rowNum) -> {
        Mpa mpa = new Mpa();
        mpa.setId(resultSet.getInt("mpa_id"));
        mpa.setName(resultSet.getString("mpa_name"));

        Film film = new Film();
        film.setId(resultSet.getLong("id"));
        film.setName(resultSet.getString("name"));
        film.setDescription(resultSet.getString("description"));
        film.setReleaseDate(resultSet.getDate("release_date").toLocalDate());
        film.setDuration(resultSet.getLong("duration"));
        film.setMpa(mpa);
        return film;
    };

    private final RowMapper<Genre> getGenreMapper = (resultSet, rowNum) -> {
        Genre genre = new Genre();
        genre.setId(resultSet.getLong("genre_id"));
        genre.setName(resultSet.getString("genre_name"));
        return genre;
    };

    private void insertFilmGenres(long filmId, Set<Genre> genres) {
        jdbcTemplate.batchUpdate(
                "INSERT INTO film_genres (film_id, genre_id) VALUES (?, ?);",
                genres,
                genres.size(),
                (PreparedStatement ps, Genre genre) -> {
                    ps.setLong(1, filmId);
                    ps.setLong(2, genre.getId());
                });
    }
}





