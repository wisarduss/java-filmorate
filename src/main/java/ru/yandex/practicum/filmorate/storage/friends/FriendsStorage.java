package ru.yandex.practicum.filmorate.storage.friends;

import ru.yandex.practicum.filmorate.model.Friend;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

public interface FriendsStorage {
    Friend create(Friend friendEntry);

    Friend update(Friend friend);

    void delete(Long friendId);

    List<Friend> getById(User user);

    Optional<Friend> getByIdUserAndFriend(User user, User friend);
}
