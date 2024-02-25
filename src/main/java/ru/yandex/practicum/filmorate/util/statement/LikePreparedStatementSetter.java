package ru.yandex.practicum.filmorate.util.statement;

import lombok.RequiredArgsConstructor;
import ru.yandex.practicum.filmorate.model.Like;
import ru.yandex.practicum.filmorate.util.statement.PreparedStatementSetter;

import java.sql.PreparedStatement;
import java.sql.SQLException;

@RequiredArgsConstructor
public class LikePreparedStatementSetter implements PreparedStatementSetter {
    private final Like like;

    public void setValues(PreparedStatement ps) throws SQLException {
        ps.setLong(1, like.getUserId());
        ps.setLong(2, like.getFilmId());
    }
}
