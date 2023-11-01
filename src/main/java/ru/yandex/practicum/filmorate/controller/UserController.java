package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import ru.yandex.practicum.filmorate.exception.FilmAndUserValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/users")
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    private final Map<Long, User> users = new HashMap<>();
    private long generateId = 0;

    @GetMapping
    public List<User> findAll() {
        log.debug("количество пользователей {}", users.size());
        return new ArrayList<>(users.values());
    }

    @PostMapping
    public User create(@RequestBody User user) {
        validate(user);
        user.setId(++generateId);
        users.put(user.getId(), user);
        log.debug("Новый пользователь добавлен {}", user);
        return user;
    }


    @PutMapping
    public User update(@RequestBody User user) {
        validate(user);
        if (users.containsKey(user.getId())) {
            users.put(user.getId(), user);
        } else {
            throw new FilmAndUserValidationException("Пользователь не найден");
        }
        log.debug("Пользователь обновлен {}", user);
        return user;
    }

    private long generationId() {
        return generateId++;
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
