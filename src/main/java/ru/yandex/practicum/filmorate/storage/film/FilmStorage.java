package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Map;

public interface FilmStorage {

    List<Film> findFilms();

    Map<Long, Film> getFilms();

    Film save(Film film);

    Film getById(long id);

    Integer getSize();

    Boolean isPresent(Film film);

    List<Film> bestByLike(Long count);

    Film generationId(Film film);


}
