package ru.yandex.practicum.filmorate.controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import ru.yandex.practicum.filmorate.exception.IdNotFoundException;
import ru.yandex.practicum.filmorate.exception.InvalidDurationTimeException;
import ru.yandex.practicum.filmorate.exception.InvalidNameException;
import ru.yandex.practicum.filmorate.exception.ReleaseDateException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/films")
public class FilmController {
    private static final int MAX_LENGTH = 200;
    private static final LocalDate MIN_DATE_RELEASE = LocalDate.of(1895, 12, 28);
    private static final Logger log = LoggerFactory.getLogger(FilmController.class);
    private final Map<Long, Film> films = new HashMap<>();
    private long generateId = 0;


    @GetMapping
    public List<Film> findAll() {
        log.debug("Количество фильмов {}", films.size());
        return new ArrayList<>(films.values());
    }

    @PostMapping
    public Film create(@RequestBody Film film) {
        if (film.getName().isBlank() || film.getName().isEmpty()) {
            throw new InvalidNameException("Имя не может быть пустым");
        }
        if (film.getDescription().length() > MAX_LENGTH) {
            throw new InvalidNameException("Описание фильма не может быть больше чем " + MAX_LENGTH + "символов");
        }
        if (film.getReleaseDate().isBefore(MIN_DATE_RELEASE)) {
            throw new ReleaseDateException("Дата релиза фильма не может быть раньше " + MIN_DATE_RELEASE);
        }
        if (film.getDuration() <= 0) {
            throw new InvalidDurationTimeException("Продолжительность фильма не может быть отрицательной");
        }
        film.setId(++generateId);
        films.put(film.getId(), film);
        log.debug("Фильм добавлен {}", film);
        return film;
    }

    @PutMapping
    public Film refresh(@RequestBody Film film) {
        if (film.getName().isBlank() || film.getName().isEmpty()) {
            throw new InvalidNameException("Имя не может быть пустым");
        }
        if (film.getName().length() > MAX_LENGTH) {
            throw new InvalidNameException("Название фильма не может быть больше чем " + MAX_LENGTH + "символов");
        }
        if (film.getReleaseDate().isBefore(MIN_DATE_RELEASE)) {
            throw new ReleaseDateException("Дата релиза фильма не может быть раньше " + MIN_DATE_RELEASE);
        }
        if (film.getDuration() <= 0) {
            throw new InvalidDurationTimeException("Продолжительность фильма не может быть отрицательной");
        }
        if (films.containsKey(film.getId())) {
            films.put(film.getId(), film);
        } else {
            throw new IdNotFoundException("Фильм не найден");
        }
        log.debug("Фильм обновлен {}", film);
        return film;
    }

    private long generationId() {
        return generateId++;
    }
}
