package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.FilmAndUserValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validator.Validate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    Validate validate;
    private static final  Logger log = LoggerFactory.getLogger(UserController.class);

    private final Map<Long, User> users = new HashMap<>();
    private long generateId = 0;

    @GetMapping
    public List<User> findAll() {
        log.debug("количество пользователей {}", users.size());
        return new ArrayList<>(users.values());
    }

    @PostMapping
    public User create(@RequestBody User user) {
        validate.validateForUser(user);
        user.setId(++generateId);
        users.put(user.getId(), user);
        log.debug("Новый пользователь добавлен {}", user);
        return user;
    }


    @PutMapping
    public User update(@RequestBody User user) {
        validate.validateForUser(user);
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
}
