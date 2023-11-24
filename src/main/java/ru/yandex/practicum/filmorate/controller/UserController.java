package ru.yandex.practicum.filmorate.controller;

import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

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

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> findAll() {
        log.info("Пользователи получены");
        return userService.findUsers();
    }

    @PostMapping
    public User create(@RequestBody User user) {
        log.info("Новый пользователь добавлен {}", user);
        return userService.create(user);
    }


    @PutMapping
    public User update(@RequestBody User user) {
        log.info("Пользователь обновлен {}", user);
        return userService.update(user);
    }

    @GetMapping("/{id}")
    public User getById(@PathVariable("id") Long id) {
        log.info("фильм с id=" + id + "получен");
        return userService.getById(id);
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
}
