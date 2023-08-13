package ru.yandex.practicum.filmorate.controller;

import ru.yandex.practicum.filmorate.exeption.ValidationException;
import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.model.User;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Slf4j
@RestController
@RequestMapping(value = "/users")
public class UserController {
    private static long counter = 0L;
    private final Map<Long, User> users = new HashMap<>();

    @GetMapping()
    public List<User> getUsers() {
        return new ArrayList<>(users.values());
    }

    @PostMapping()
    public User add(@RequestBody User user) {
        check(user);
        final long id = ++counter;
        user.setId(id);
        users.put(id, user);
        return user;
    }

    @PutMapping()
    public User update(@RequestBody User user) {
        if (users.get(user.getId()) == null) {
            throw new ValidationException("Not found key: " + user.getId());
        }
        check(user);
        users.put(user.getId(), user);
        return user;
    }

    private void check(User user) {
        log.info("лог.пришел запрос Post /films с телом: request");
        if (user.getEmail().isEmpty() || !user.getEmail().contains("@")) {
            log.debug("электронная почта не может быть пустой и должна содержать символ @");
            throw new ValidationException("электронная почта не может быть пустой и должна содержать символ @");
        } else if (user.getLogin().isEmpty() || user.getLogin().contains(" ")) {
            log.debug("логин не может быть пустым и содержать пробелы");
            throw new ValidationException("логин не может быть пустым и содержать пробелы");
        } else if (user.getName().isEmpty()) {
            user.setName(user.getLogin());
            users.put(user.getId(), user);
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
