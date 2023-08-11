package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import java.time.Duration;
import java.time.LocalDate;

@Data
public class Film {
    private static long idFilm = 0;

    private final long id = ++idFilm;
    private final String name;
    private final String description;
    private final LocalDate releaseDate;
    private final Duration duration;
}
