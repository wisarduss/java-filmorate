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
import ru.yandex.practicum.filmorate.model.Film;
import  ru.yandex.practicum.filmorate.validator.Validate;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/films")
public class FilmController {
    Validate validate = new Validate();

    private static final Logger log = LoggerFactory.getLogger(FilmController.class);
    private final Map<Long, Film> films = new HashMap<>();
    private long generateId = 0;


    @GetMapping
    public List<Film> findAll() {
        log.debug("количество фильмов {}", films.size());
        return new ArrayList<>(films.values());
    }

    @PostMapping
    public Film create(@RequestBody Film film) {
        validate.validateForFilm(film);
        film.setId(++generateId);
        films.put(film.getId(), film);
        log.debug("Фильм добавлен {}", film);
        return film;
    }

    @PutMapping
    public Film update(@RequestBody Film film) {
        validate.validateForFilm(film);
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
