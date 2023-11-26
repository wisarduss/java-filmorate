package ru.yandex.practicum.filmorate.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private static final Logger log = LoggerFactory.getLogger(FilmService.class);

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public List<Film> findFilms() {
        return filmStorage.findFilms();
    }

    public Film create(Film film) {
        return filmStorage.create(film);
    }

    public Film getById(long id) {
        return filmStorage.getById(id);
    }

    public Film update(Film film) {
        return filmStorage.update(film);
    }

    public void addNewLike(long filmId, long userId) {
        Film film = filmStorage.getById(filmId);
        User user = userStorage.getById(userId);
        log.info("пользователь с userId=" + userId + " поставил like фильму с filmId=" + filmId);
        film.getLikes().add(user.getId());
    }

    public void deleteLike(long filmId, long userId) {
        Film film = filmStorage.getById(filmId);
        User user = userStorage.getById(userId);
        log.info("пользователь с userId=" + userId + " удалил like фильму с filmId=" + filmId);
        film.getLikes().remove(user.getId());
    }

    public List<Film> bestByLike(Long count) {
        return filmStorage.findFilms().stream()
                .sorted(Comparator.comparing(film -> film.getLikes().size(), Comparator.reverseOrder()))
                .limit(count).collect(Collectors.toList());
    }


}
