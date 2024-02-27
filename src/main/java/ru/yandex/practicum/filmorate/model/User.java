package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
public class User {
    private Long id;
    @Email(message = "Email не корректный")
    private String email;
    @NotBlank(message = "Login не может быть пустым")
    @Pattern(regexp = "^\\S*$")
    private String login;
    private String name;
    @PastOrPresent(message = "День рождение не может быть в прошлом")
    private LocalDate birthday;

}