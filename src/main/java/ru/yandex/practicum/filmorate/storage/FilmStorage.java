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
    void addLike(int filmId, int userId);
    void deleteLike(long filmId, long userId);
    List<Film> topFilms(Integer id);
    public List<Genre> genres();

    public Genre genre(int id);

    public List<MPA> allMpa();

    public MPA mpa(int id);


}
