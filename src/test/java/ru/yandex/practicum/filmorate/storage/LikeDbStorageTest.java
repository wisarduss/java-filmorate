package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Like;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.db.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.db.LikeDbStorage;
import ru.yandex.practicum.filmorate.storage.db.MpaDbStorage;
import ru.yandex.practicum.filmorate.storage.db.UserDbStorage;
import ru.yandex.practicum.filmorate.util.DatabaseUtil;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@JdbcTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class LikeDbStorageTest {
    private final JdbcTemplate jdbcTemplate;
    private LikeDbStorage likeDbStorage;
    private User user;
    private User user2;
    private Film film;;

    @BeforeEach
    public void setUp() {
        DatabaseUtil databaseUtil = new DatabaseUtil(jdbcTemplate);
        likeDbStorage = new LikeDbStorage(jdbcTemplate, databaseUtil);
        FilmDbStorage filmDbStorage = new FilmDbStorage(jdbcTemplate, databaseUtil);
        UserDbStorage userDbStorage = new UserDbStorage(jdbcTemplate, databaseUtil);
        MpaDbStorage mpaDbStorage = new MpaDbStorage(jdbcTemplate);
        user = createUser(userDbStorage);
        user2 = createUser(userDbStorage);
        film = createFilm(filmDbStorage, mpaDbStorage);
    }

    @Test
    public void testShouldReturnCreateUserAndLikeFilm() {
        Like newLike = new Like();
        newLike.setUserId(user.getId());
        newLike.setFilmId(film.getId());
        likeDbStorage.create(newLike);

        Optional<Like> savedLike = likeDbStorage.getByIdFilmAndUser(film, user);

        assertTrue(savedLike.isPresent());
        assertEquals(savedLike.get().getFilmId(), film.getId());
        assertEquals(savedLike.get().getUserId(), user.getId());
    }

    @Test
    public void testShouldReturnDeleteLike() {
        Like newLike = new Like();
        newLike.setUserId(user.getId());
        newLike.setFilmId(film.getId());
        likeDbStorage.create(newLike);
        Optional<Like> savedLike = likeDbStorage.getByIdFilmAndUser(film, user);

        likeDbStorage.delete(savedLike.get().getId());

        assertTrue(likeDbStorage.getByIdFilmAndUser(film, user).isEmpty());
    }

    @Test
    public void testShouldReturnFilmLikes() {
        List<Like> emptyLikes = likeDbStorage.getByFilmId(film);

        Like newLike = new Like();
        newLike.setUserId(user.getId());
        newLike.setFilmId(film.getId());
        likeDbStorage.create(newLike);

        List<Like> LikesWithOneItem = likeDbStorage.getByFilmId(film);

        Like newLike2 = new Like();
        newLike2.setUserId(user2.getId());
        newLike2.setFilmId(film.getId());
        likeDbStorage.create(newLike2);

        List<Like> LikesWithTwoItems = likeDbStorage.getByFilmId(film);

        assertEquals(emptyLikes.size(), 0);
        assertEquals(LikesWithOneItem.size(), 1);
        assertEquals(LikesWithTwoItems.size(), 2);
    }

    private User createUser(UserDbStorage userDbStorage) {
        User user = new User();
        user.setName("testName");
        user.setLogin("wisardus");
        user.setBirthday(LocalDate.of(2003, 5, 10));
        user.setEmail("test@gmail.com");
        return userDbStorage.create(user);
    }

    private Film createFilm(FilmDbStorage filmDbStorage, MpaDbStorage mpaDbStorage) {
        Mpa mpa = new Mpa();
        mpa.setName("test");
        Film film = new Film();
        film.setName("test");
        film.setDescription("testDesc");
        film.setDuration(90);
        film.setReleaseDate(LocalDate.of(2015, 7, 12));
        film.setMpa(mpaDbStorage.getMpaById(1));
        film.setGenres(new HashSet<>());
        return filmDbStorage.create(film);
    }

}
