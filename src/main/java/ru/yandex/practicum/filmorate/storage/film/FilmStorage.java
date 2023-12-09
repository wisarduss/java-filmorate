package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {

    List<Film> findFilms();

    Film update(Film film);

    Film getById(long id);

    Film create(Film film);
}
