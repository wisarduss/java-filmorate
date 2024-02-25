package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.DataNotFoundException;
import ru.yandex.practicum.filmorate.exception.UserAlreadyExistException;
import ru.yandex.practicum.filmorate.storage.Mpa.MpaStorage;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;
import ru.yandex.practicum.filmorate.storage.like.LikeStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Like;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.LinkedHashSet;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private final LikeStorage likeStorage;
    private final MpaStorage mpaStorage;
    private final GenreStorage genreStorage;


    public List<Film> findFilms() {
        return filmStorage.findFilms();
    }

    public Film create(Film film) {
        Film addedMapAndGenreToFilm = addMpaAndGenres(film);
        return filmStorage.create(film);
    }

    public Film getById(long id) {
        return filmStorage.getById(id);
    }

    public Film update(Film film) {
        Film addedMapAndGenreToFilm = addMpaAndGenres(film);
        return filmStorage.update(film);
    }

    public void addNewLike(long filmId, long userId) {
        Film film = filmStorage.getById(filmId);
        User user = userStorage.getById(userId);
        if (likeStorage.getByIdFilmAndUser(film, user).isPresent()) {
            throw new UserAlreadyExistException("Пользователь с id = " + userId + " уже поставил лайк");
        }
        Like like = new Like();
        like.setFilmId(film.getId());
        like.setUserId(user.getId());
        likeStorage.create(like);
    }

    public void deleteLike(long filmId, long userId) {
        Film film = filmStorage.getById(filmId);
        User user = userStorage.getById(userId);
        Optional<Like> like = likeStorage.getByIdFilmAndUser(film, user);
        if (like.isEmpty()) {
            throw new DataNotFoundException("Пользователь с id =  " + userId + " не поставил лайк",
                    HttpStatus.NOT_FOUND);
        }
        likeStorage.delete(like.get().getId());
    }

    public List<Film> bestByLike(Long count) {
        List<Film> films = filmStorage.findFilms();
        films.sort(Comparator.comparingInt((Film film) -> likeStorage.getByFilmId(film).size()));
        films = films.subList((int) Math.max(films.size() - count, 0), films.size());
        Collections.reverse(films);
        return films;
    }

    public Film getById(Integer filmId) {
        return filmStorage.getById(filmId);
    }

    public Film addMpaAndGenres(Film film) {
        Mpa mpa = film.getMpa();
        if (mpa != null) {
            Mpa existedMpa = mpaStorage.getMpaById(mpa.getId());
            film.setMpa(existedMpa);
        }

        List<Long> genreIds = film.getGenres().stream()
                .map(Genre::getId)
                .collect(Collectors.toList());
        film.setGenres(new LinkedHashSet<>(genreStorage.getByIds(genreIds)));
        return film;
    }


}
