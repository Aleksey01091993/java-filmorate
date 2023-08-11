package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class User {
    private static long userId = 0;


    private final long id = ++userId;
    private String email;
    private String login;
    private String name;
    private LocalDate birthday;
}
