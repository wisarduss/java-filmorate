package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Map;

public interface UserStorage {
    List<User> findAllUsers();

    User save(User user);

    Map<Long, User> getUsers();

    User getById(long id);

    Integer getSize();

    Boolean isPresent(User user);

    List<User> findCommonFriends(User user, User otherUser);

    User generationId(User user);
}
