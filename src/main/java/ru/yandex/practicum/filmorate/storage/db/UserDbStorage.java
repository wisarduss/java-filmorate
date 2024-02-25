package ru.yandex.practicum.filmorate.storage.db;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.DataNotFoundException;
import ru.yandex.practicum.filmorate.exception.FilmAndUserValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import ru.yandex.practicum.filmorate.util.DatabaseUtil;
import ru.yandex.practicum.filmorate.util.statements.UserPreparedStatement;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;
    private final DatabaseUtil databaseUtil;

    @Override
    public List<User> findUsers() {
        String sqlQuery = "SELECT id, email, login, name, birthday" +
                " FROM users;";
        return jdbcTemplate.query(sqlQuery, getUserMapper);
    }

    @Override
    public User create(User user) {
        validate(user);
        String sql = "INSERT INTO users (email, login, name, birthday)" +
                " VALUES (?, ?, ?, ?);";
        long userId = databaseUtil.insertAndReturnId(sql, new UserPreparedStatement(user));
        user.setId(userId);
        return user;
    }

    @Override
    public User update(User user) {
        validate(user);
        Long userId = user.getId();
        String sqlQuery = "UPDATE users SET email = ?, login = ?, name = ?, birthday = ?" +
                " where id = ?";
        int update = jdbcTemplate.update(
                sqlQuery, user.getEmail(), user.getLogin(), user.getName(), Date.valueOf(user.getBirthday()), userId
        );
        if (update == 0) {
            throw new DataNotFoundException("Пользователь с id =  " + userId + " не найден",HttpStatus.NOT_FOUND);
        }
        return user;
    }

    @Override
    public User getById(long id) {
        String sqlQuery = "SELECT id, email, login, name, birthday" +
                "  FROM users" +
                " WHERE id = ?;";
        try {
            return jdbcTemplate.queryForObject(sqlQuery, getUserMapper, id);
        } catch (EmptyResultDataAccessException e) {
            throw new DataNotFoundException("Пользователь с id = " + id + " не найден",HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public void delete(long id) {
        String sqlQuery = "DELETE FROM users" +
                " where id = ?";
        int update = jdbcTemplate.update(sqlQuery, id);
        if (update == 0) {
            throw new DataNotFoundException("Пользователь с id =  " + id + " не найден",HttpStatus.NOT_FOUND);
        }
    }


    private final RowMapper<User> getUserMapper = (resultSet, rowNum) -> {
        User user = new User();
        user.setId(resultSet.getLong("id"));
        user.setEmail(resultSet.getString("email"));
        user.setLogin(resultSet.getString("login"));
        user.setName(resultSet.getString("name"));
        user.setBirthday(resultSet.getDate("birthday").toLocalDate());
        return user;
    };


    private void validate(User user) {
        if (user.getEmail() == null || user.getEmail().isBlank() || (!user.getEmail().contains("@"))) {
            throw new FilmAndUserValidationException("Ваш email не может быть пустым или в нем не указан символ '@'");
        }
        if (user.getLogin().isBlank() || user.getLogin() == null || user.getLogin().contains(" ")) {
            throw new FilmAndUserValidationException("Неверный логин");
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new FilmAndUserValidationException("Дата вашего рождения не может быть в 'будущем'");
        }
        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
    }

}


