package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Duration;
import java.time.LocalDate;

@Data
@AllArgsConstructor
public class Film {
    private static long idFilm = 0;

    private final long id = ++idFilm;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private Duration duration;
}
