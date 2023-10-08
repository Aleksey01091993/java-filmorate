package ru.yandex.practicum.filmorate.storage.dao;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

public interface GenreDao {
    List<Genre> genres();

    Genre genre(int id);

    void load(List<Film> films);

    List<Genre> loadGenre(int id);


}
