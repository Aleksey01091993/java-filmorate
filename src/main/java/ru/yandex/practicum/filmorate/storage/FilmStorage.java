package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;

import java.util.List;



public interface FilmStorage {
    List<Film> getFilms();
    Film add(Film film);
    Film update(Film film);
    Film getFilm(int id);
    List<Film> topFilms(Integer id);


}
