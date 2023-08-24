package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;


public interface FilmStorage {
    public List<Film> getFilms();
    public Film add(Film film);
    public Film update(Film film);
}
