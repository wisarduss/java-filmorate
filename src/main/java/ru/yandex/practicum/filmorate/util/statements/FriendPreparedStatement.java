package ru.yandex.practicum.filmorate.util.statements;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.PreparedStatementSetter;
import ru.yandex.practicum.filmorate.model.Friend;

import java.sql.PreparedStatement;
import java.sql.SQLException;

@RequiredArgsConstructor
public class FriendPreparedStatement implements PreparedStatementSetter {
    private final Friend friend;

    public void setValues(PreparedStatement ps) throws SQLException {
        ps.setLong(1, friend.getUserId());
        ps.setLong(2, friend.getFriendId());
        ps.setString(3, friend.getFriendStatus().name());
    }
}
