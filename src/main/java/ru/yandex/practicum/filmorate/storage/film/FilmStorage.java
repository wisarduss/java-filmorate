package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Map;

public interface FilmStorage {

    List<Film> findFilms();

    Map<Long, Film> getFilms();

    Film update(Film film);

    Film getById(long id);

    Film create(Film film);
}
