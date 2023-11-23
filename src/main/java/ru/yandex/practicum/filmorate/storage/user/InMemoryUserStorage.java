package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.IncorrectIdException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class InMemoryUserStorage implements UserStorage {
    private final Map<Long, User> users = new HashMap<>();
    private long generateId = 0;

    @Override
    public List<User> findAllUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public Map<Long, User> getUsers() {
        return users;
    }

    @Override
    public User getById(long id) {
        if (users.containsKey(id)) {
            return users.get(id);
        } else {
            throw new IncorrectIdException("Пользователя с id = " + id + " не существует");
        }
    }

    @Override
    public User save(User user) {
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public Integer getSize() {
        return users.size();
    }

    @Override
    public Boolean isPresent(User user) {
        return users.containsKey(user.getId());
    }

    @Override
    public User generationId(User user) {
        user.setId(++generateId);
        return user;
    }

    @Override
    public List<User> findCommonFriends(User user, User otherUser) {
        List<User> commonFriends = new ArrayList<>();
        for (Long friend : user.getFriends()) {
            if (otherUser.getFriends().contains(friend)) {
                commonFriends.add(getById(friend));
            }
        }
        return commonFriends;
    }


}
