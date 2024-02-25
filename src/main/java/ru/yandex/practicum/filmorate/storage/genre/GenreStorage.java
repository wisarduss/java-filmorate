package ru.yandex.practicum.filmorate.storage.genre;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

public interface GenreStorage {
    List<Genre> findGenres();

    Genre getGenreById(long id);

    List<Genre> getByIds(List<Long> genreIds);
}
