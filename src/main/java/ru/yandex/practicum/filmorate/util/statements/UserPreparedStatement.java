package ru.yandex.practicum.filmorate.util.statements;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.PreparedStatementSetter;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@RequiredArgsConstructor
public class UserPreparedStatement implements PreparedStatementSetter {
    private final User user;

    public void setValues(PreparedStatement ps) throws SQLException {
        ps.setString(1, user.getEmail());
        ps.setString(2, user.getLogin());
        ps.setString(3, user.getName());
        ps.setDate(4, Date.valueOf(user.getBirthday()));
    }
}
