package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.FilmAndUserValidationException;
import ru.yandex.practicum.filmorate.exception.IncorrectIdException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class InMemoryFilmStorage implements FilmStorage {
    private static final int MAX_LENGTH = 200;
    private static final LocalDate MIN_DATE_RELEASE = LocalDate.of(1895, 12, 28);
    private final Map<Long, Film> films = new HashMap<>();
    private long generateId = 0;

    @Override
    public List<Film> findFilms() {
        return new ArrayList<>(films.values());
    }

    @Override
    public Map<Long, Film> getFilms() {
        return films;
    }

    @Override
    public Film create(Film film) {
        validate(film);
        film.setId(++generateId);
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Film update(Film film) {
        validate(film);
        if (films.containsKey(film.getId())) {
            films.put(film.getId(), film);
        } else {
            throw new IncorrectIdException("Фильм не найден");
        }
        return film;
    }

    @Override
    public Film getById(long id) {
        if (films.containsKey(id)) {
            return films.get(id);
        } else {
            throw new IncorrectIdException("фильма с id=" + id + " не существует");
        }
    }

    private void validate(Film film) {
        if (film.getName() == null || film.getName().isBlank()) {
            throw new FilmAndUserValidationException("Имя не может быть пустым");
        }
        if (film.getDescription().length() > MAX_LENGTH) {
            throw new FilmAndUserValidationException("Описание фильма не может быть больше чем "
                    + MAX_LENGTH + " символов");
        }
        if (film.getReleaseDate().isBefore(MIN_DATE_RELEASE)) {
            throw new FilmAndUserValidationException("Дата релиза фильма не может быть раньше " + MIN_DATE_RELEASE);
        }
        if (film.getDuration() <= 0) {
            throw new FilmAndUserValidationException("Продолжительность фильма не может быть отрицательной");
        }
    }
}
