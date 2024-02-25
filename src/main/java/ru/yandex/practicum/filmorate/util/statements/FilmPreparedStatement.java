package ru.yandex.practicum.filmorate.util.statements;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.PreparedStatementSetter;
import ru.yandex.practicum.filmorate.model.Film;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@RequiredArgsConstructor
public class FilmPreparedStatement implements PreparedStatementSetter {
    private final Film film;

    public void setValues(PreparedStatement ps) throws SQLException {
        ps.setString(1, film.getName());
        ps.setString(2, film.getDescription());
        ps.setDate(3, Date.valueOf(film.getReleaseDate()));
        ps.setLong(4, film.getDuration());
        ps.setLong(5, film.getMpa().getId());
    }
}
