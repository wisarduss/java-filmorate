package ru.yandex.practicum.filmorate.util.statements;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.PreparedStatementSetter;
import ru.yandex.practicum.filmorate.model.Like;

import java.sql.PreparedStatement;
import java.sql.SQLException;

@RequiredArgsConstructor
public class LikePreparedStatement implements PreparedStatementSetter {
    private final Like like;

    public void setValues(PreparedStatement ps) throws SQLException {
        ps.setLong(1, like.getUserId());
        ps.setLong(2, like.getFilmId());
    }
}
