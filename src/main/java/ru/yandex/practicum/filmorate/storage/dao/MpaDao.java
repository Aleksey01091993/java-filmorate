package ru.yandex.practicum.filmorate.storage.dao;

import ru.yandex.practicum.filmorate.model.MPA;

import java.util.List;

public interface MpaDao {

    List<MPA> allMpa();

    MPA mpa(int id);
}
