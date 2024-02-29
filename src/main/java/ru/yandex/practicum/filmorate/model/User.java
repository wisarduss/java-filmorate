package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;

@Data
public class User {
    private Long id;
    @NotEmpty
    @Email(message = "Email не корректный")
    private String email;
    @NotBlank(message = "Login не может быть пустым")
    @Pattern(regexp = "^\\S*$")
    private String login;
    private String name;
    @NotNull
    @PastOrPresent(message = "День рождение не может быть в прошлом")
    private LocalDate birthday;
}