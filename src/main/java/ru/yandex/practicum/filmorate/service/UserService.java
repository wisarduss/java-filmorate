package ru.yandex.practicum.filmorate.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    private final UserStorage userStorage;
    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public List<User> findUsers() {
        return userStorage.findUsers();
    }

    public User create(User user) {
        return userStorage.create(user);
    }

    public User update(User user) {
        return userStorage.update(user);
    }

    public User getById(long id) {
        return userStorage.getById(id);
    }

    public void addNewFriend(long userId, long friendId) {
        User user = userStorage.getById(userId);
        User friend = userStorage.getById(friendId);
        log.info("Стали друзьями пользователи c ID=" + userId + " и с ID=" + friendId);
        user.getFriends().add(friendId);
        friend.getFriends().add(userId);
    }

    public void removeFriend(long userId, long friendId) {
        User user = userStorage.getById(userId);
        User friend = userStorage.getById(friendId);
        log.info("пользователь с " + userId + " перестал дружить с другом " + friendId);
        user.getFriends().remove(friendId);
        friend.getFriends().remove(userId);
    }

    public List<User> getFriendsOfUser(long userId) {
        List<User> friends = new ArrayList<>();
        User user = userStorage.getById(userId);
        for (Long friendId : user.getFriends()) {
            friends.add(userStorage.getById(friendId));
        }
        return friends;
    }

    public List<User> findCommonFriends(long userId, long friendId) {
        User user = userStorage.getById(userId);
        User friendUser = userStorage.getById(friendId);
        List<User> commonFriends = new ArrayList<>();
        for (Long friend : user.getFriends()) {
            if (friendUser.getFriends().contains(friend)) {
                commonFriends.add(getById(friend));
            }
        }
        return commonFriends;
    }
}
