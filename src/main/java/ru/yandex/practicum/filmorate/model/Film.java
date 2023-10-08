package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    private Integer mpaId;
    private MPA mpa;
    private List<Genre> genre;
    @NotBlank
    private String name;
    @Size(max = 200)
    private String description;
    private LocalDate releaseDate;
    @Positive
    private float duration;
    @JsonIgnore
    private Set<Integer> likes;

    public Film() {
    }

    public Film(String name, String description, LocalDate releaseDate, float duration, Integer mpaId) {
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.mpaId = mpaId;
        this.likes = new HashSet<>();
        this.genre = new ArrayList<>();
    }
}
