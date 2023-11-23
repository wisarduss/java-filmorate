package ru.yandex.practicum.filmorate.controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.yandex.practicum.filmorate.exception.FilmAndUserValidationException;
import ru.yandex.practicum.filmorate.exception.IncorrectIdException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;


@RestController
@RequestMapping("/films")
public class FilmController {
    private static final int MAX_LENGTH = 200;
    private static final LocalDate MIN_DATE_RELEASE = LocalDate.of(1895, 12, 28);
    private static final Logger log = LoggerFactory.getLogger(FilmController.class);

    private final FilmService filmService;
    private final FilmStorage filmStorage;

    @Autowired
    public FilmController(FilmService filmService, FilmStorage filmStorage) {
        this.filmService = filmService;
        this.filmStorage = filmStorage;
    }

    @GetMapping
    public List<Film> findAll() {
        log.debug("количество фильмов {}", filmStorage.getSize());
        return filmStorage.findFilms();
    }

    @PostMapping
    public Film create(@RequestBody Film film) {
        validate(film);
        filmStorage.generationId(film);
        filmStorage.save(film);
        log.debug("Фильм добавлен {}", film);
        return film;
    }

    @GetMapping("/{id}")
    public Film getById(@PathVariable("id") Long id) {
        log.info("фильм с id=" + id + " получен");
        return filmStorage.getById(id);
    }

    @PutMapping
    public Film update(@RequestBody Film film) {
        validate(film);
        if (filmStorage.isPresent(film)) {
            filmStorage.save(film);
        } else {
            throw new IncorrectIdException("Фильм не найден");
        }
        log.debug("Фильм обновлен {}", film);
        return film;
    }

    @PutMapping(value = "/{id}/like/{userId}")
    public void likeFilm(@PathVariable Long id, @PathVariable Long userId) {
        filmService.addNewLike(id, userId);
        log.info("Лайк получен");
    }

    @DeleteMapping(value = "/{id}/like/{userId}")
    public void deleteLikeFilm(@PathVariable Long id, @PathVariable Long userId) {
        filmService.deleteLike(id, userId);
        log.info("Лайк удален");
    }

    @GetMapping(value = "/popular")
    public Collection<Film> bestFilms(@RequestParam(name = "count", defaultValue = "10", required = false) Long count) {
        List<Film> bestsFilms = filmService.bestByLike(count);
        log.info("Все фильмы получены " + bestsFilms.size());
        return bestsFilms;
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
