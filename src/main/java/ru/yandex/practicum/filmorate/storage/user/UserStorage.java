package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Map;

public interface UserStorage {
    List<User> findUsers();

    User create(User user);

    User update(User user);

    Map<Long, User> getUsers();

    User getById(long id);
}
