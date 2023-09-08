package ru.yandex.practicum.filmorate.storage;


import ru.yandex.practicum.filmorate.model.User;

import java.util.List;



public interface UserStorage {
    List<User> getUsers();
    User add(User user);
    User update(User user);
    User getUser(Long id);
}
