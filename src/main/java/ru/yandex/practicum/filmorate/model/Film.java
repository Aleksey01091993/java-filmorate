package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.Duration;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;


@Data
public class Film {
    private Integer id;
    private String mpa;
    private String genre;
    @NotBlank
    private String name;
    @Size(max = 200)
    private String description;
    private LocalDate releaseDate;
    @Positive
    private float duration;
    private Set<Integer> likes;

    public Film() {
    }

    public Film(String name, String description, LocalDate releaseDate, float duration, String genre, String mpa) {
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.likes = new HashSet<>();
        this.genre = genre;
        this.mpa = mpa;
    }
}
