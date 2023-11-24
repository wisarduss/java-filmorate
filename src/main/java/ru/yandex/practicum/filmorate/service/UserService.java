package ru.yandex.practicum.filmorate.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.IncorrectIdException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
        Map<Long, User> allUsers = userStorage.getUsers();
        if (!allUsers.containsKey(userId)) {
            throw new IncorrectIdException("пользователя с " + userId + " не существует");
        }
        if (!allUsers.containsKey(friendId)) {
            throw new IncorrectIdException("друга с " + friendId + " не существует");
        }
        log.info("Стали друзьями пользователи c ID=" + userId + " и с ID=" + friendId);
        allUsers.get(userId).getFriends().add(friendId);
        allUsers.get(friendId).getFriends().add(userId);
    }

    public void removeFriend(long userId, long friendId) {
        Map<Long, User> allUsers = userStorage.getUsers();
        if (!allUsers.containsKey(userId)) {
            throw new IncorrectIdException("пользователя с " + userId + " не существует");
        }
        if (!allUsers.containsKey(friendId)) {
            throw new IncorrectIdException("друга с " + friendId + " не существует");
        }
        log.info("пользователь с " + userId + " перестал дружить с другом " + friendId);
        allUsers.get(userId).getFriends().remove(friendId);
        allUsers.get(friendId).getFriends().remove(userId);
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
        if (user == null) {
            throw new IncorrectIdException("Пользователя с userId=" + userId + " не существует");
        }
        if (friendUser == null) {
            throw new IncorrectIdException("Друга с userId=" + friendId + " не существует");
        }
        for (Long friend : user.getFriends()) {
            if (friendUser.getFriends().contains(friend)) {
                commonFriends.add(getById(friend));
            }
        }
        return commonFriends;
    }

}
