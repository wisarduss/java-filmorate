package ru.yandex.practicum.filmorate.storage.db;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.DataNotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.Mpa.MpaStorage;

import java.util.List;


@Component
@RequiredArgsConstructor
public class MpaDbStorage implements MpaStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Mpa> findMpa() {
        String sqlQuery = "SELECT id, name" +
                " FROM mpa;";
        return jdbcTemplate.query(sqlQuery, getMpaMapper);
    }

    @Override
    public Mpa getMpaById(long id) {
        String sqlQuery = "SELECT id, name" +
                " FROM mpa" +
                " WHERE id = ?;";
        try {
            return jdbcTemplate.queryForObject(sqlQuery, getMpaMapper, id);
        } catch (EmptyResultDataAccessException e) {
            throw new DataNotFoundException("Рейтинг с id =  " + id + " не найден",HttpStatus.NOT_FOUND);
        }
    }

    private final RowMapper<Mpa> getMpaMapper = (resultSet, rowNum) -> {
        Mpa mpa = new Mpa();
        mpa.setId(resultSet.getLong("id"));
        mpa.setName(resultSet.getString("name"));
        return mpa;
    };

}
