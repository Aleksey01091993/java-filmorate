package ru.yandex.practicum.filmorate.storage;


import ru.yandex.practicum.filmorate.exeption.DataNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;



public interface UserStorage {
    List<User> getUsers();
    User add(User user);
    User update(User user) throws DataNotFoundException;
    User getUser(int id) throws DataNotFoundException;

}
