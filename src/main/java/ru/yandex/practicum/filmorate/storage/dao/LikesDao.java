package ru.yandex.practicum.filmorate.storage.dao;

public interface LikesDao {
    void addLike(int filmId, int userId);
    void deleteLike(long filmId, long userId);
}
