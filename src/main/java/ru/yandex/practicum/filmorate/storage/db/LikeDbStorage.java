package ru.yandex.practicum.filmorate.storage.db;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.DataNotFoundException;
import ru.yandex.practicum.filmorate.model.*;
import ru.yandex.practicum.filmorate.storage.like.LikeStorage;
import ru.yandex.practicum.filmorate.util.DatabaseUtil;
import ru.yandex.practicum.filmorate.util.statement.LikePreparedStatementSetter;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RequiredArgsConstructor
@Repository
public class LikeDbStorage implements LikeStorage {
    private final JdbcTemplate jdbcTemplate;
    private final DatabaseUtil databaseUtil;

    @Override
    public Like create(Like like) {
        String sqlQuery = "INSERT INTO likes (user_id,film_id) " +
                "VALUES (?,?)";
        long likeId = databaseUtil.insertAndReturnId(sqlQuery, new LikePreparedStatementSetter(like));
        like.setId(likeId);
        return like;
    }

    @Override
    public void delete(Long likeId) {
        String sqlQuery = "DELETE FROM likes where id = ?";
        int update = jdbcTemplate.update(sqlQuery, likeId);
        if (update == 0) {
            throw new DataNotFoundException("Лайк с id = " + likeId + " не найден");
        }
    }

    @Override
    public List<Like> getByFilmId(Film film) {
        String sqlQuery = "SELECT * FROM likes " +
                "WHERE film_id = ?";
        return jdbcTemplate.query(sqlQuery, getLikeMapper, film.getId());
    }

    @Override
    public Optional<Like> getByIdFilmAndUser(Film film, User user) {
        String sqlQuery = "SELECT * FROM likes " +
                "WHERE film_id = ? AND user_id = ?";
        try {
            return Optional.of(jdbcTemplate.queryForObject(sqlQuery, getLikeMapper,
                    film.getId(), user.getId()));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Film> findPopular(long count) {;
        return jdbcTemplate.query(
                "SELECT f.id, f.name, f.description, f.release_date, f.duration, f.mpa_id, mpa.name AS mpa_name, " +
                        "string_agg(g.id || ',' || g.name, ';') AS genres, " +
                        "COUNT(ufl.user_id) AS user_like_count " +
                        "FROM films f " +
                        "LEFT JOIN mpa ON f.mpa_id = mpa.id " +
                        "LEFT JOIN likes ufl on f.id = ufl.film_id " +
                        "LEFT JOIN film_genres AS fg ON f.id = fg.film_id " +
                        "LEFT JOIN genres AS g ON fg.genre_id = g.id " +
                        "GROUP BY f.id " +
                        "ORDER BY user_like_count DESC " +
                        "LIMIT ?",
                LikeDbStorage::mapRowToFilm,
                count);
    }

    private final RowMapper<Like> getLikeMapper = (resultSet, rowNum) -> {
        Like like = new Like();
        like.setId(resultSet.getLong("id"));
        like.setFilmId(resultSet.getLong("film_id"));
        like.setUserId(resultSet.getLong("user_id"));
        return like;
    };

    public static Film mapRowToFilm(ResultSet row, int rowNum) throws SQLException {
        Long id = row.getLong("id");
        String name = row.getString("name");
        String description = row.getString("description");
        LocalDate releaseDate = row.getDate("release_date").toLocalDate();
        long duration = row.getLong("duration");
        long mpaId = row.getInt("mpa_id");
        String mpaName = row.getString("mpa_name");
        Mpa mpa = new Mpa(mpaId, mpaName);
        Film film = new Film(id, name, description, releaseDate, duration, mpa);

        String genreRowData = row.getString("genres");
        Set<Genre> genreSet = new HashSet<>();

        if (genreRowData != null && !genreRowData.isEmpty() && !genreRowData.isBlank()) {
            String[] genreRow = genreRowData.split(";");
            for (String s : genreRow) {
                String[] finalGenre = s.split(",");
                long genreId = Long.parseLong(finalGenre[0]);
                String genreName = finalGenre[1];
                Genre genre = new Genre(genreId, genreName);
                genreSet.add(genre);
            }
        }
        film.setGenres(genreSet);
        return film;
    }
}
