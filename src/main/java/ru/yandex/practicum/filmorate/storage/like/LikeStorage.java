package ru.yandex.practicum.filmorate.storage.like;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Like;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

public interface LikeStorage {
    Like create(Like like);

    void delete(Long likeId);

    List<Like> getByFilmId(Film film);

    Optional<Like> getByIdFilmAndUser(Film film, User user);

    public List<Film> findPopular(long count);
}
