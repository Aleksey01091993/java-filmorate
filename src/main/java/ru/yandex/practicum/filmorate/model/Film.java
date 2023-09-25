package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.Data;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Data
public class Film {
    private Integer id;
    private String mpa;
    private List<String> genre;
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

    public Film(String name, String description, LocalDate releaseDate, float duration, String mpa) {
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.likes = new HashSet<>();
        this.genre = new ArrayList<>();
        this.mpa = mpa;
    }
}
