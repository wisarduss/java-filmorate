package ru.yandex.practicum.filmorate.validator;

import ru.yandex.practicum.filmorate.exception.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;


public class Validate {
    private static final int MAX_LENGTH = 200;
    private static final LocalDate MIN_DATE_RELEASE = LocalDate.of(1895, 12, 28);

    public void validateForFilm(Film film) {
        if (film.getName() == null || film.getName().isBlank()) {
            throw new InvalidNameException("Имя не может быть пустым");
        }
        if (film.getDescription().length() > MAX_LENGTH) {
            throw new InvalidDescriptionException("Описание фильма не может быть больше чем "
                    + MAX_LENGTH + " символов");
        }
        if (film.getReleaseDate().isBefore(MIN_DATE_RELEASE)) {
            throw new ReleaseDateException("Дата релиза фильма не может быть раньше " + MIN_DATE_RELEASE);
        }
        if (film.getDuration() <= 0) {
            throw new InvalidDurationTimeException("Продолжительность фильма не может быть отрицательной");
        }
    }

    public void validateForUser(User user) {
        if (user.getEmail() == null || user.getEmail().isBlank() || (!user.getEmail().contains("@"))) {
            throw new InvalidEmailException("Ваш email не может быть пустым или в нем не указан символ '@'");
        }
        if (user.getLogin().isBlank() || user.getLogin() == null || user.getLogin().contains(" ")) {
            throw new InvalidLoginException("Неверный логин");
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new InvalidBirthdayException("Дата вашего рождения не может быть в 'будущем'");
        }
        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
    }
}
