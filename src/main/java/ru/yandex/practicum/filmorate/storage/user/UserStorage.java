package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {
    List<User> findUsers();

    User create(User user);

    User update(User user);

    User getById(long id);

    void delete(long id);
}
