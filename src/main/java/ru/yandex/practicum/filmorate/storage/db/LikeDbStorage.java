package ru.yandex.practicum.filmorate.storage.db;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.DataNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Like;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.like.LikeStorage;
import ru.yandex.practicum.filmorate.util.DatabaseUtil;
import ru.yandex.practicum.filmorate.util.statement.LikePreparedStatementSetter;

import java.util.List;
import java.util.Optional;

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

    private final RowMapper<Like> getLikeMapper = (resultSet, rowNum) -> {
        Like like = new Like();
        like.setId(resultSet.getLong("id"));
        like.setFilmId(resultSet.getLong("film_id"));
        like.setUserId(resultSet.getLong("user_id"));
        return like;
    };
}
