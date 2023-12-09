package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.FilmAndUserValidationException;
import ru.yandex.practicum.filmorate.exception.IncorrectIdException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class InMemoryUserStorage implements UserStorage {
    private final Map<Long, User> users = new HashMap<>();
    private long generateId = 0;

    @Override
    public List<User> findUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User create(User user) {
        validate(user);
        user.setId(++generateId);
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User update(User user) {
        validate(user);
        if (!users.containsKey(user.getId())) {
            throw new IncorrectIdException("Пользователь не найден");
        }
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User getById(long id) {
        if (!users.containsKey(id)) {
            throw new IncorrectIdException("Пользователя с id = " + id + " не существует");
        } else {
            return users.get(id);
        }
    }

    private void validate(User user) {
        if (user.getEmail() == null || user.getEmail().isBlank() || (!user.getEmail().contains("@"))) {
            throw new FilmAndUserValidationException("Ваш email не может быть пустым или в нем не указан символ '@'");
        }
        if (user.getLogin().isBlank() || user.getLogin() == null || user.getLogin().contains(" ")) {
            throw new FilmAndUserValidationException("Неверный логин");
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new FilmAndUserValidationException("Дата вашего рождения не может быть в 'будущем'");
        }
        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
    }
}