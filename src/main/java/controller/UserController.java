package controller;

import exeption.ValidationException;
import lombok.extern.slf4j.Slf4j;
import model.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Slf4j
@RestController
public class UserController {
    private Map<String, User> users = new HashMap<>();

    @GetMapping("/users")
    public List<User> getUsers() {
        return new ArrayList<>(users.values());
    }

    @PostMapping(value = "/user")
    public User add(@RequestBody User user) {
        if (user.getEmail().isEmpty() || !user.getEmail().contains("@")) {
            log.debug("электронная почта не может быть пустой и должна содержать символ @");
            throw new ValidationException("электронная почта не может быть пустой и должна содержать символ @");
        } else if (user.getLogin().isEmpty() || user.getLogin().contains(" ")) {
            log.debug("логин не может быть пустым и содержать пробелы");
            throw new ValidationException("логин не может быть пустым и содержать пробелы");
        } else if (user.getName().isEmpty()) {
            User user1 = new User(user.getId(), user.getEmail(), user.getLogin(), user.getLogin(),
                    user.getBirthday());
            users.put(user1.getName(), user1);
            log.debug("имя для отображения может быть пустым — в таком случае будет использован логин");
            throw new ValidationException("имя для отображения может быть пустым — в таком случае будет использован логин");
        } else if (user.getBirthday().isAfter(LocalDate.now())) {
            log.debug("дата рождения не может быть в будущем");
            throw new ValidationException("дата рождения не может быть в будущем");
        } else {
            users.put(user.getName(), user);
            return user;
        }
    }
}
