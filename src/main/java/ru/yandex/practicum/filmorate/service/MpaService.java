package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.storage.dao.MpaDao;

import java.util.List;

public class MpaService {

    private final MpaDao storage;


    public MpaService(@Autowired @Qualifier("MpaDaoImpl") MpaDao storage) {
        this.storage = storage;
    }

    public List<MPA> allMpa() {
        return storage.allMpa();
    }

    public MPA mpa(int id) {
        return  storage.mpa(id);
    }
}
