package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

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

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping
    public List<Film> findAll() {
        log.info("Все фильмы получены");
        return filmService.findFilms();
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        log.info("Фильм добавлен {}", film);
        return filmService.create(film);
    }

    @GetMapping("/{id}")
    public Film getById(@PathVariable("id") Long id) {
        log.info("фильм с id = {} получен", id);
        return filmService.getById(id);
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        log.info("Фильм обновлен {}", film);
        return filmService.update(film);
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
    public Collection<Film> bestFilms(@RequestParam(name = "count", defaultValue = "10") Long count) {
        List<Film> bestsFilms = filmService.bestByLike(count);
        log.info("Все фильмы получены " + bestsFilms.size());
        return bestsFilms;
    }
}
