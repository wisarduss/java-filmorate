package ru.yandex.practicum.filmorate.model;

import lombok.Data;


import java.time.LocalDate;

@Data
public class Film {
    protected long id;
    protected String name;
    protected String description;
    protected LocalDate releaseDate;
    protected long duration;
}
