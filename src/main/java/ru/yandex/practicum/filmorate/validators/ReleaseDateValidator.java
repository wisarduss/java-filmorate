package ru.yandex.practicum.filmorate.validators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class ReleaseDateValidator implements ConstraintValidator<ValidReleaseDate, LocalDate> {
    private LocalDate initializeLocalDate;

    @Override
    public void initialize(ValidReleaseDate constraint) {
        initializeLocalDate = LocalDate.parse(constraint.value());

    }

    @Override
    public boolean isValid(LocalDate localDate, ConstraintValidatorContext context) {
        return !initializeLocalDate.isAfter(localDate);
    }
}
