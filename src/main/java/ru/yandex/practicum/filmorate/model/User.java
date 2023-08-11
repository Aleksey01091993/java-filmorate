package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import java.time.LocalDate;

@Data
public class User {
    private static long userId = 0;


    private final long id = ++userId;
    private final String email;
    private final String login;
    private final String name;
    private final LocalDate birthday;
}
