package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.dao.GenreDao;

import java.util.List;

@Service
public class GenreService {

    private final GenreDao storage;


    public GenreService(@Autowired @Qualifier("GenreDaoImpl") GenreDao storage) {
        this.storage = storage;
    }

    public List<Genre> genres() {
        return storage.genres();
    }

    public Genre genre(int id) {
        return  storage.genre(id);
    }
}
