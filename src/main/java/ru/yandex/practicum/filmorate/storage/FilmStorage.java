package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;



public interface FilmStorage {
    List<Film> getFilms();
    Film add(Film film);
    Film update(Film film);
    Film getFilm(int id);
    void addLike(int filmId, int userId);
    void deleteLike(long filmId, long userId);
    List<Film> topFilms(Integer id);
}
