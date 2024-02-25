package ru.yandex.practicum.filmorate.storage.db;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.DataNotFoundException;
import ru.yandex.practicum.filmorate.model.Friend;
import ru.yandex.practicum.filmorate.model.FriendStatus;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.friends.FriendsStorage;
import ru.yandex.practicum.filmorate.util.DatabaseUtil;
import ru.yandex.practicum.filmorate.util.statements.FriendPreparedStatement;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class FriendsDbStorage implements FriendsStorage {
    private final JdbcTemplate jdbcTemplate;

    private final DatabaseUtil databaseUtil;

    @Override
    public Friend create(Friend friend) {
        String sqlQuery = "INSERT INTO friends (user_id, friend_id, friend_status) " +
                "VALUES (?,?,?)";
        long friendId = databaseUtil.insertAndReturnId(sqlQuery, new FriendPreparedStatement(friend));
        friend.setId(friendId);
        return friend;
    }

    @Override
    public Friend update(Friend friend) {
        Long friendId = friend.getId();
        String sqlQuery = "UPDATE friends SET friend_status = ? where id = ?";
        int update = jdbcTemplate.update(
                sqlQuery, friend.getFriendStatus().name(), friend.getId()
        );
        if (update == 0) {
            throw new DataNotFoundException("Друг с id = " + friendId + " не найден", HttpStatus.NOT_FOUND);
        }
        return friend;
    }

    @Override
    public void delete(Long friendId) {
        String sqlQuery = "DELETE FROM friends where id = ?";
        int update = jdbcTemplate.update(sqlQuery, friendId);
        if (update == 0) {
            throw new DataNotFoundException("Друг с id =  " + friendId + " не найден", HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public List<Friend> getById(User user) {
        String sqlQuery = "SELECT * FROM friends f" +
                " WHERE (f.user_id = ?) OR (f.friend_id = ? AND f.friend_status = 'CONFIRMED');";
        return jdbcTemplate.query(sqlQuery, getFriendRowMapper, user.getId(), user.getId());
    }

    @Override
    public Optional<Friend> getByIdUserAndFriend(User user, User friend) {
        String sqlQuery = "SELECT * FROM friends " +
                "WHERE user_id = ? and friend_id = ?;";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sqlQuery, getFriendRowMapper,
                    user.getId(), friend.getId()));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    private final RowMapper<Friend> getFriendRowMapper = (resultSet, rowNum) -> {
        Friend friend = new Friend();
        friend.setId(resultSet.getLong("id"));
        friend.setUserId(resultSet.getLong("user_id"));
        friend.setFriendId(resultSet.getLong("friend_id"));
        friend.setFriendStatus(FriendStatus.valueOf(resultSet.getString("friend_status")));
        return friend;
    };
}
