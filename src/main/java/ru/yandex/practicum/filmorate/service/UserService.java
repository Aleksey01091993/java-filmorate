package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exeption.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;

@Slf4j
@Service
public class UserService {

    private final UserStorage storage;

    public UserService(@Autowired @Qualifier("UserDbStorage") UserStorage storage) {
        this.storage = storage;
    }

    public List<User> getUsers() {
        return storage.getUsers();
    }

    public User getUser(int id) {
        return storage.getUser(id);
    }

    public User add(User user) {
        check(user);
        return storage.add(user);
    }

    public User update(User user) {
        check(user);
        return storage.update(user);
    }

    public void addFriend(int userId, int friendId) {
        storage.addFriend(userId, friendId);
    }

    public void deleteFriend(long userId, long friendId) {
        storage.deleteFriend(userId, friendId);
    }

    public List<User> friends(long userId) {
        return storage.friends(userId);
    }

    public List<User> mutualFriends(long userId, long friendId) {
        return storage.mutualFriends(userId, friendId);
    }

    private void check(User user) {
        log.info("лог.пришел запрос Post /Users с телом: request");
        if (user.getName().isEmpty()) {
            user.setName(user.getLogin());
            log.debug("имя для отображения может быть пустым — в таком случае будет использован логин");
            throw new ValidationException("имя для отображения может быть пустым — в таком случае будет использован логин");
        } else {
            log.info("отправлен ответ с телом: response");
        }
    }

}
