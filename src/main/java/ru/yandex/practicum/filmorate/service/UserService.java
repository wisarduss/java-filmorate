package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.DataNotFoundException;
import ru.yandex.practicum.filmorate.exception.UserAlreadyExistException;
import ru.yandex.practicum.filmorate.model.Friend;
import ru.yandex.practicum.filmorate.model.FriendStatus;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.friends.FriendsStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserStorage userStorage;
    private final FriendsStorage friendsStorage;

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
        if (userId == friendId) {
            throw new UserAlreadyExistException("Пользователь не может доавить себя в друзья");
        }
        User user = userStorage.getById(userId);
        User friend = userStorage.getById(friendId);

        if (friendsStorage.getByIdUserAndFriend(user, friend).isPresent()) {
            throw new UserAlreadyExistException("Пользователь с id= " + friendId + " уже добавлен");
        }

        Optional<Friend> friendFriendEntryOpt = friendsStorage.getByIdUserAndFriend(friend, user);
        if (friendFriendEntryOpt.isPresent()) {
            Friend friendFriendEntry = friendFriendEntryOpt.get();
            friendFriendEntry.setFriendStatus(FriendStatus.CONFIRMED);
            friendsStorage.update(friendFriendEntry);
            return;
        }

        Friend userFriend = new Friend();
        userFriend.setUserId(user.getId());
        userFriend.setFriendId(friend.getId());
        userFriend.setFriendStatus(FriendStatus.UNCONFIRMED);
        friendsStorage.create(userFriend);
    }

    public void removeFriend(long userId, long friendId) {
        User user = userStorage.getById(userId);
        User friend = userStorage.getById(friendId);

        Optional<Friend> userFriendEntryOpt = friendsStorage.getByIdUserAndFriend(user, friend);
        if (userFriendEntryOpt.isPresent()) {
            friendsStorage.delete(userFriendEntryOpt.get().getId());
            return;
        }

        Optional<Friend> friendFriendEntryOpt = friendsStorage.getByIdUserAndFriend(friend, user);
        if (friendFriendEntryOpt.isPresent()) {
            Friend friendFriendEntry = friendFriendEntryOpt.get();
            friendFriendEntry.setFriendStatus(FriendStatus.UNCONFIRMED);
            friendsStorage.update(friendFriendEntry);
            return;
        }

        throw new DataNotFoundException("Пользователь с id = " + friendId + " Не находится в друзьях");

    }

    public List<User> getFriendsOfUser(long userId) {
        User user = userStorage.getById(userId);
        List<Friend> friend = friendsStorage.getById(user);
        List<User> friends = new ArrayList<>();
        for (Friend friendEntry : friend) {
            if (friendEntry.getUserId().equals(userId)) {
                friends.add(userStorage.getById(friendEntry.getFriendId()));
            } else {
                friends.add(userStorage.getById(friendEntry.getUserId()));
            }
        }
        friends.sort(Comparator.comparingLong(User::getId));
        return friends;
    }

    public List<User> findCommonFriends(long userId, long friendId) {
        List<Friend> userFriend = friendsStorage.getById(userStorage.getById(userId));
        List<Friend> otherFriend = friendsStorage.getById(userStorage.getById(friendId));

        Set<Long> userFriends = userFriend.stream()
                .map(friend -> friend.getUserId().equals(userId) ? friend.getFriendId() : friend.getUserId())
                .collect(Collectors.toSet());

        Set<Long> otherFriends = otherFriend.stream()
                .map(friend -> friend.getUserId().equals(friendId) ? friend.getFriendId() : friend.getUserId())
                .collect(Collectors.toSet());
        userFriends.retainAll(otherFriends);
        return userFriends.stream()
                .map(userStorage::getById)
                .collect(Collectors.toList());
    }

}
