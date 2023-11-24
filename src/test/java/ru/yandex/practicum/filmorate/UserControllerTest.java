package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.FilmAndUserValidationException;
import ru.yandex.practicum.filmorate.exception.IncorrectIdException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.List;

public class UserControllerTest {
    UserStorage userStorage = new InMemoryUserStorage();
    UserService userService = new UserService(userStorage);
    UserController userController = new UserController(userService);


    @Test
    public void shouldReturnUserWhenCreate() {
        User user = new User("qwerty@mail.ru", "wisardus", "Max",
                LocalDate.of(2003, 5, 10));
        userController.create(user);
        List<User> users = userController.findAll();
        assertEquals(1, users.size());
        assertEquals("[" + user + "]", users.toString());

    }

    @Test
    public void shouldReturnInvalidEmailExceptionWhenCreate() {
        User user = new User("", "wisardus", "Max",
                LocalDate.of(2003, 5, 10));

        FilmAndUserValidationException exception = assertThrows(
                FilmAndUserValidationException.class,
                () -> userController.create(user)
        );
        assertEquals("Ваш email не может быть пустым или в нем не указан символ '@'", exception.getMessage());
    }

    @Test
    public void shouldReturnInvalidEmailExceptionWhenCreateWithoutSymbol() {
        User user = new User("qwertymail.ru", "wisardus", "Max",
                LocalDate.of(2003, 5, 10));

        FilmAndUserValidationException exception = assertThrows(
                FilmAndUserValidationException.class,
                () -> userController.create(user)
        );
        assertEquals("Ваш email не может быть пустым или в нем не указан символ '@'", exception.getMessage());
    }

    @Test
    public void shouldReturnInvalidLoginExceptionWhenCreate() {
        User user = new User("qwerty@mail.ru", "", "Max",
                LocalDate.of(2003, 5, 10));

        FilmAndUserValidationException exception = assertThrows(
                FilmAndUserValidationException.class,
                () -> userController.create(user)
        );
        assertEquals("Неверный логин", exception.getMessage());
    }

    @Test
    public void shouldReturnInvalidLoginExceptionWhenCreateWithSpace() {
        User user = new User("qwerty@mail.ru", "wisa rdus", "Max",
                LocalDate.of(2003, 5, 10));

        FilmAndUserValidationException exception = assertThrows(
                FilmAndUserValidationException.class,
                () -> userController.create(user)
        );
        assertEquals("Неверный логин", exception.getMessage());
    }

    @Test
    public void shouldReturnUserWhenCreateWithoutName() {
        User user = new User("qwerty@mail.ru", "wisardus", "",
                LocalDate.of(2003, 5, 10));

        userController.create(user);
        assertEquals("wisardus", user.getName());
    }

    @Test
    public void shouldReturnInvalidBirthdayExceptionWhenCreate() {
        User user = new User("qwerty@mail.ru", "wisardus", "Max",
                LocalDate.of(2025, 5, 10));

        FilmAndUserValidationException exception = assertThrows(
                FilmAndUserValidationException.class,
                () -> userController.create(user)
        );
        assertEquals("Дата вашего рождения не может быть в 'будущем'", exception.getMessage());
    }

    @Test
    public void shouldReturnInvalidEmailExceptionWhenCreateWithEmptyUser() {
        User user = new User();
        FilmAndUserValidationException exception = assertThrows(
                FilmAndUserValidationException.class,
                () -> userController.create(user)
        );
        assertEquals("Ваш email не может быть пустым или в нем не указан символ '@'", exception.getMessage());
    }

    @Test
    public void shouldReturnNewFilmWhenUpdate() {
        User user = new User("qwerty@mail.ru", "wisardus", "Max",
                LocalDate.of(2003, 5, 10));
        userController.create(user);

        User newUser = new User(1, "qwerty@mail.ru", "postaground", "Max",
                LocalDate.of(2003, 5, 10));

        userController.update(newUser);
        assertEquals("[" + newUser + "]", userController.findAll().toString());
    }

    @Test
    public void shouldReturnIdNotFoundExceptionWhenUpdate() {
        User user = new User("qwerty@mail.ru", "wisardus", "Max",
                LocalDate.of(2003, 5, 10));
        userController.create(user);

        User newUser = new User(123, "qwerty@mail.ru", "postaground", "Max",
                LocalDate.of(2003, 5, 10));

        IncorrectIdException exception = assertThrows(
                IncorrectIdException.class,
                () -> userController.update(newUser)
        );
        assertEquals("Пользователь не найден", exception.getMessage());
    }
}
