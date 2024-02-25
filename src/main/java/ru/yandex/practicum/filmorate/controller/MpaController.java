package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.MpaService;

import java.util.List;

@RestController
@RequestMapping("/mpa")
@RequiredArgsConstructor
public class MpaController {
    private final MpaService mpaService;
    private static final Logger log = LoggerFactory.getLogger(MpaController.class);

    @GetMapping
    public List<Mpa> getAll() {
        final List<Mpa> ratings = mpaService.findMpa();
        log.info("Рейтинги получены");
        return ratings;
    }

    @GetMapping("/{id}")
    public Mpa getMpaById(@PathVariable long id) {
        log.info("Рейтинг с id=" + id + "получен");
        return mpaService.getMpaById(id);
    }

}
