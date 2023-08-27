package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exeption.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage{

    private final Map<Long, User> users = new HashMap<>();
    private static long counter = 0L;
    @Override
    public List<User> getUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User add(User user) {
        check(user);
        final long id = ++counter;
        user.setId(id);
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User update(User user) {
        check(user);
        if (users.get(user.getId()) == null) {
            throw new ValidationException("Not found key: " + user.getId());
        }
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User getUser(Long id) {
        return users.get(id);
    }

    private void check(User user) {
        log.info("лог.пришел запрос Post /Users с телом: request");
        if (user.getEmail().isEmpty() || !user.getEmail().contains("@")) {
            log.debug("электронная почта не может быть пустой и должна содержать символ @");
            throw new ValidationException("электронная почта не может быть пустой и должна содержать символ @");
        } else if (user.getLogin().isEmpty() || user.getLogin().contains(" ")) {
            log.debug("логин не может быть пустым и содержать пробелы");
            throw new ValidationException("логин не может быть пустым и содержать пробелы");
        } else if (user.getName().isEmpty()) {
            user.setName(user.getLogin());
            log.debug("имя для отображения может быть пустым — в таком случае будет использован логин");
            throw new ValidationException("имя для отображения может быть пустым — в таком случае будет использован логин");
        } else if (user.getBirthday().isAfter(LocalDate.now())) {
            log.debug("дата рождения не может быть в будущем");
            throw new ValidationException("дата рождения не может быть в будущем");
        } else {
            log.info("отправлен ответ с телом: response");
        }
    }
}
