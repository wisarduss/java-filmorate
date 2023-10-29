package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.IdNotFoundException;
import ru.yandex.practicum.filmorate.exception.InvalidBirthdayException;
import ru.yandex.practicum.filmorate.exception.InvalidLoginException;
import ru.yandex.practicum.filmorate.exception.InvalidNameException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/users")
public class UserController {
    private static final  Logger log = LoggerFactory.getLogger(UserController.class);

    private final Map<Long, User> users = new HashMap<>();
    private long generateId = 0;

    @GetMapping
    public List<User> findAll() {
        log.debug("Количество пользователей {}", users.size());
        return new ArrayList<>(users.values());
    }

    @PostMapping
    public User create(@RequestBody User user) {
        if (user.getEmail() == null || user.getEmail().isBlank() || (!user.getEmail().contains("@"))) {
            throw new InvalidNameException("Ваш email не может быть пустым или в нем не указан символ '@'");
        }
        if (user.getLogin().isBlank() || user.getLogin() == null || user.getLogin().contains(" ")) {
            throw new InvalidLoginException("Неверный логин");
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new InvalidBirthdayException("Дата вашего рождения не может быть в 'будущем'");
        }
        if (user.getName() == null) {
            user.setName(user.getLogin());
        }
        user.setId(++generateId);
        users.put(user.getId(), user);
        log.debug("Новый пользователь добавлен {}", user);
        return user;
    }


    @PutMapping
    public User refresh(@RequestBody User user) {
        if (user.getEmail().isEmpty() || user.getEmail().isBlank() || (!user.getEmail().contains("@"))) {
            throw new InvalidNameException("Ваш email не может быть пустым или в нем не указан символ '@'");
        }
        if (user.getLogin().isBlank() || user.getLogin().isEmpty() || user.getLogin().contains(" ")) {
            throw new InvalidLoginException("Неверный логин");
        }
        if (user.getName().isBlank() || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new InvalidBirthdayException("Дата вашего рождения не может быть в 'будущем'");
        }
        if (users.containsKey(user.getId())) {
            users.put(user.getId(), user);
        } else {
            throw new IdNotFoundException("Пользователь не найден");
        }
        log.debug("Пользователь обновлен {}", user);
        return user;
    }

    private long generationId() {
        return generateId++;
    }
}
