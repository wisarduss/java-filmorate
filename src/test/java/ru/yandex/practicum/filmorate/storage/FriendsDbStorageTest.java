package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.model.Friend;
import ru.yandex.practicum.filmorate.model.FriendStatus;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.db.FriendsDbStorage;
import ru.yandex.practicum.filmorate.storage.db.UserDbStorage;
import ru.yandex.practicum.filmorate.util.DatabaseUtil;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@JdbcTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FriendsDbStorageTest {
    private final JdbcTemplate jdbcTemplate;
    private FriendsDbStorage friendsDbStorage;
    private User user1;
    private User user2;
    private User user3;


    @BeforeEach
    public void setUp() {
        DatabaseUtil databaseUtil = new DatabaseUtil(jdbcTemplate);
        friendsDbStorage = new FriendsDbStorage(jdbcTemplate, databaseUtil);
        UserDbStorage userDbStorage = new UserDbStorage(jdbcTemplate, databaseUtil);
        user1 = createUser(userDbStorage);
        user2 = createUser(userDbStorage);
        user3 = createUser(userDbStorage);
    }

    @Test
    public void testShouldReturnCreate() {
        Friend friend = new Friend();
        friend.setUserId(user1.getId());
        friend.setFriendId(user2.getId());
        friend.setFriendStatus(FriendStatus.UNCONFIRMED);
        friendsDbStorage.create(friend);

        Optional<Friend> savedFriend = friendsDbStorage.getByIdUserAndFriend(user1, user2);

        assertTrue(savedFriend.isPresent());
        assertEquals(savedFriend.get().getUserId(), user1.getId());
        assertEquals(savedFriend.get().getFriendId(), user2.getId());
    }

    @Test
    public void testShouldReturnUpdate() {
        Friend friend = new Friend();
        friend.setUserId(user1.getId());
        friend.setFriendId(user2.getId());
        friend.setFriendStatus(FriendStatus.UNCONFIRMED);
        Friend savedFriend = friendsDbStorage.create(friend);

        assertEquals(savedFriend.getFriendStatus(), FriendStatus.UNCONFIRMED);

        savedFriend.setFriendStatus(FriendStatus.CONFIRMED);
        friendsDbStorage.update(savedFriend);
        Friend savedFriendEntryAfterChange = friendsDbStorage.getByIdUserAndFriend(user1, user2).get();

        assertEquals(savedFriendEntryAfterChange.getFriendStatus(), FriendStatus.CONFIRMED);
    }

    @Test
    public void testShouldReturnGetById() {
        List<Friend> emptySavedFriends = friendsDbStorage.getById(user1);

        Friend friend = new Friend();
        friend.setUserId(user1.getId());
        friend.setFriendId(user2.getId());
        friend.setFriendStatus(FriendStatus.UNCONFIRMED);
        friendsDbStorage.create(friend);

        List<Friend> friendsWithOneItem = friendsDbStorage.getById(user1);

        Friend friendEntry2 = new Friend();
        friendEntry2.setUserId(user3.getId());
        friendEntry2.setFriendId(user1.getId());
        friendEntry2.setFriendStatus(FriendStatus.CONFIRMED);
        friendsDbStorage.create(friendEntry2);

        List<Friend> friendsWithTwoItems = friendsDbStorage.getById(user1);

        assertEquals(emptySavedFriends.size(), 0);
        assertEquals(friendsWithOneItem.size(), 1);
        assertEquals(friendsWithTwoItems.size(), 2);
    }

    @Test
    public void testShouldReturnDelete() {
        Friend friend = new Friend();
        friend.setUserId(user1.getId());
        friend.setFriendId(user2.getId());
        friend.setFriendStatus(FriendStatus.UNCONFIRMED);
        friendsDbStorage.create(friend);
        Optional<Friend> friendEntry = friendsDbStorage.getByIdUserAndFriend(user1, user2);

        friendsDbStorage.delete(friendEntry.get().getId());

        assertTrue(friendsDbStorage.getByIdUserAndFriend(user1, user2).isEmpty());
    }

    private User createUser(UserDbStorage userDbStorage) {
        User user = new User();
        user.setName("testName");
        user.setLogin("wisardus");
        user.setBirthday(LocalDate.of(2003, 5, 10));
        user.setEmail("test@gmail.com");
        return userDbStorage.create(user);
    }
}
