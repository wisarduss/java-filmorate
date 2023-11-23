package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.IncorrectIdException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Comparator;
import java.util.stream.Collectors;

@Component
public class InMemoryFilmStorage implements FilmStorage {

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
    public Film getById(long id) {
        if (films.containsKey(id)) {
            return films.get(id);
        } else {
            throw new IncorrectIdException("фильма с id=" + id + " не существует");
        }
    }

    @Override
    public List<Film> bestByLike(Long count) {
        return findFilms().stream()
                .sorted(Comparator.comparing(film -> film.getLikes().size(), Comparator.reverseOrder()))
                .limit(count).collect(Collectors.toList());
    }

    @Override
    public Film save(Film film) {
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Integer getSize() {
        return films.size();
    }

    @Override
    public Boolean isPresent(Film film) {
        return films.containsKey(film.getId());
    }

    @Override
    public Film generationId(Film film) {
        film.setId(++generateId);
        return film;
    }
}
