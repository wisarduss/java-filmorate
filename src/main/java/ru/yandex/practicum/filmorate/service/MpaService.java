package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.Mpa.MpaStorage;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MpaService {
    private static final Logger log = LoggerFactory.getLogger(FilmService.class);
    private final MpaStorage mpaStorage;

    public List<Mpa> findMpa() {
        return mpaStorage.findMpa();
    }

    public Mpa getMpaById(long id) {
        log.info("Рейтинг с id=" + id + "получен");
        return mpaStorage.getMpaById(id);
    }

}
