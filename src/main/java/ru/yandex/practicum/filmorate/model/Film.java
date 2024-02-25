package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import ru.yandex.practicum.filmorate.validators.ValidReleaseDate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
public class Film {
    private long id;
    @NonNull
    @NotBlank
    private String name;
    @NonNull
    @Size(max = 200, min = 1)
    private String description;
    @NonNull
    @ValidReleaseDate(value = "1895-12-28")
    private LocalDate releaseDate;
    @NonNull
    @Positive
    private long duration;
    private Set<Long> likes = new HashSet<>();
    private Set<Genre> genres = new HashSet<>();
    private Mpa mpa;
}
