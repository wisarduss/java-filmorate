package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.DeleteMapping;
import ru.yandex.practicum.filmorate.exception.FilmAndUserValidationException;
import ru.yandex.practicum.filmorate.exception.IncorrectIdException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;

import java.util.Collection;
import java.util.List;


@RestController
@RequestMapping("/users")
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private final UserStorage userStorage;
    private final UserService userService;

    @Autowired
    public UserController(UserStorage userStorage, UserService userService) {
        this.userStorage = userStorage;
        this.userService = userService;
    }

    @GetMapping
    public List<User> findAll() {
        log.debug("количество пользователей {}", userStorage.getSize());
        return userStorage.findAllUsers();
    }

    @PostMapping
    public User create(@RequestBody User user) {
        validate(user);
        userStorage.generationId(user);
        userStorage.save(user);
        log.debug("Новый пользователь добавлен {}", user);
        return user;
    }


    @PutMapping
    public User update(@RequestBody User user) {
        validate(user);
        if (userStorage.isPresent(user)) {
            userStorage.save(user);
        } else {
            throw new IncorrectIdException("Пользователь не найден");
        }
        log.debug("Пользователь обновлен {}", user);
        return user;
    }

    @GetMapping("/{id}")
    public User getById(@PathVariable("id") Long id) {
        log.info("фильм с id=" + id + "получен");
        return userStorage.getById(id);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addNewFriend(@PathVariable("id") Long id, @PathVariable("friendId") Long friendId) {
        log.info("пользователь с id=" + id + " и пользователь с friendId=" + friendId + " стали друзьями");
        userService.addNewFriend(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void removeFriend(@PathVariable("id") Long id, @PathVariable("friendId") Long friendId) {
        log.info("пользователь с id=" + id + " и пользователь с friendId=" + friendId + " перестали дружить");
        userService.removeFriend(id, friendId);
    }

    @GetMapping("/{id}/friends")
    public List<User> getFriendsOfUser(@PathVariable("id") Long id) {
        log.info("Друзья пользователя с id=" + id + " получены");
        return userService.getFriendsOfUser(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public Collection<User> getCommonFriends(@PathVariable("id") Long id, @PathVariable("otherId") Long otherId) {
        log.info("список общих друзей пользователя с id=" + id + " и друга с friendId=" + otherId + "получены ");
        return userService.findCommonFriends(id, otherId);
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
