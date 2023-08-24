package ru.yandex.practicum.filmorate.controller;

import ru.yandex.practicum.filmorate.exeption.ValidationException;
import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.model.User;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.List;


@Slf4j
@RestController
@RequestMapping(value = "/users")
public class UserController {
    private static long counter = 0L;
    private final User user;
    private final UserStorage storage;

    public UserController(UserService service) {
        this.user = service.getUser();
        this.storage = service.getStorage();
    }

    @GetMapping()
    public List<User> getUsers() {
        return storage.getUsers();
    }

    @PostMapping()
    public User add(@RequestBody User user) {
        check(user);
        final long id = ++counter;
        user.setId(id);
        storage.add(user);
        return user;
    }

    @PutMapping()
    public User update(@RequestBody User user) {
        check(user);
        storage.update(user);
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
