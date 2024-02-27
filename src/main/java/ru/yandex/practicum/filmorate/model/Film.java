package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import ru.yandex.practicum.filmorate.validators.ValidReleaseDate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class Film {
    private long id;
    @NotBlank
    private String name;
    @Size(max = 200, min = 1)
    private String description;
    @ValidReleaseDate(value = "1895-12-28")
    private LocalDate releaseDate;
    @Positive
    private long duration;
    private Set<Genre> genres = new HashSet<>();
    private Mpa mpa;
}
