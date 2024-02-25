package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.exception.DataNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.db.UserDbStorage;
import ru.yandex.practicum.filmorate.util.DatabaseUtil;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@JdbcTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserDbStorageTest {
    private final JdbcTemplate jdbcTemplate;
    private UserDbStorage userDbStorage;

    @BeforeEach
    public void setUp() {
        DatabaseUtil databaseUtil = new DatabaseUtil(jdbcTemplate);
        userDbStorage = new UserDbStorage(jdbcTemplate, databaseUtil);
    }

    @Test
    public void testShouldReturnCreateUser() {
        User user = new User();
        user.setName("testName");
        user.setEmail("test@gmail.com");
        user.setLogin("wisardus");
        user.setBirthday(LocalDate.of(2003, 5, 10));
        userDbStorage.create(user);

        User savedUser = userDbStorage.getById(1);
        assertEquals(savedUser.getName(), user.getName());
        assertEquals(savedUser.getLogin(), user.getLogin());
        assertEquals(savedUser.getBirthday(), user.getBirthday());
        assertEquals(savedUser.getEmail(), user.getEmail());
    }

    @Test
    public void testShouldReturnUpdateUser() {
        User user = new User();
        user.setName("testName");
        user.setEmail("test@gmail.com");
        user.setLogin("wisardus");
        user.setBirthday(LocalDate.of(2003, 5, 10));
        User savedUser = userDbStorage.create(user);

        savedUser.setName("testNameqwerty");
        savedUser.setEmail("test2@gmail.com");
        userDbStorage.update(savedUser);
        User savedUserAfterChange = userDbStorage.getById(1);
        assertEquals(savedUserAfterChange.getName(), "testNameqwerty");
        assertEquals(savedUserAfterChange.getEmail(), "test2@gmail.com");
    }

    @Test
    public void testShouldReturnDeleteUser() {
        User user = new User();
        user.setName("testName");
        user.setEmail("test@gmail.com");
        user.setLogin("wisarduss");
        user.setBirthday(LocalDate.of(2003, 5, 10));
        User savedUser = userDbStorage.create(user);

        userDbStorage.delete(savedUser.getId());
        assertThrows(DataNotFoundException.class, () -> {
            userDbStorage.getById(1);
        });
        assertThrows(DataNotFoundException.class, () -> {
            userDbStorage.delete(2);
        });
    }

    @Test
    public void testShouldReturnGetByIdUser() {
        User user = new User();
        user.setName("testName");
        user.setEmail("test@gmail.com");
        user.setLogin("wisardus");
        user.setBirthday(LocalDate.of(2003, 5, 10));
        userDbStorage.create(user);

        User savedUser = userDbStorage.getById(1);
        assertEquals(savedUser.getName(), user.getName());
        assertEquals(savedUser.getEmail(), user.getEmail());
    }

    @Test
    public void testShouldReturnFindAllUser() {
        User user = new User();
        user.setName("testName");
        user.setEmail("test@gmail.com");
        user.setLogin("wisardus");
        user.setBirthday(LocalDate.of(2003, 5, 10));
        userDbStorage.create(user);

        User user2 = new User();
        user2.setName("testName");
        user2.setEmail("test@gmail.com");
        user2.setLogin("wisardus");
        user2.setBirthday(LocalDate.of(2003, 5, 10));
        userDbStorage.create(user2);

        assertEquals(userDbStorage.findUsers().size(), 2);
        assertEquals(userDbStorage.findUsers().get(0), user);
        assertEquals(userDbStorage.findUsers().get(1), user2);
    }
}
