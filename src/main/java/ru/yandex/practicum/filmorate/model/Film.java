package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.Data;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.validation.annotation.Validated;

import java.time.Duration;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;


@Data
public class Film {
    private long id;
    private MPA mpa;
    private Genre genre;
    @NotBlank
    private String name;
    @Size(max = 200)
    private String description;
    private LocalDate releaseDate;
    @Positive
    private Duration duration;
    private Set<Long> likes;

    public Film(String name, String description, LocalDate releaseDate, Duration duration, Genre genre, MPA mpa) {
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.likes = new HashSet<>();
        this.genre = genre;
        this.mpa = mpa;
    }
}
