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

@Service
public class UserService {
    private final UserStorage userStorage;
    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public void addNewFriend(long userId, long friendId) {
        if (!userStorage.getUsers().containsKey(userId)) {
            throw new IncorrectIdException("пользователя с " + userId + " не существует");
        }
        if (!userStorage.getUsers().containsKey(friendId)) {
            throw new IncorrectIdException("друга с " + friendId + " не существует");
        }
        log.info("Стали друзьями пользователи c ID=" + userId + " и с ID=" + friendId);
        userStorage.getUsers().get(userId).getFriends().add(friendId);
        userStorage.getUsers().get(friendId).getFriends().add(userId);
    }

    public void removeFriend(long userId, long friendId) {
        if (!userStorage.getUsers().containsKey(userId)) {
            throw new IncorrectIdException("пользователя с " + userId + " не существует");
        }
        if (!userStorage.getUsers().containsKey(friendId)) {
            throw new IncorrectIdException("друга с " + friendId + " не существует");
        }
        log.info("пользователь с " + userId + " перестал дружить с другом " + friendId);
        userStorage.getUsers().get(userId).getFriends().remove(friendId);
        userStorage.getUsers().get(friendId).getFriends().remove(userId);
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
        User friend = userStorage.getById(friendId);
        if (user == null) {
            throw new IncorrectIdException("Пользователя с userId=" + userId + " не существует");
        }
        if (friend == null) {
            throw new IncorrectIdException("Друга с userId=" + friendId + " не существует");
        }
        return userStorage.findCommonFriends(user, friend);
    }


}
