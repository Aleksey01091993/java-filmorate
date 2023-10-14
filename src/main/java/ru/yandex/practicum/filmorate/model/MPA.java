package ru.yandex.practicum.filmorate.model;

import lombok.Data;

@Data
public class MPA {
    private int id;
    private String mpa;

    public MPA(int id, String mpa) {
        this.id = id;
        this.mpa = mpa;
    }
}
