package ru.yandex.practicum.filmorate.model;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;



import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
//
@Data
public class User {
    private Integer id;
    @Email
    private String email;
    @NotBlank
    private String login;
    private String name;
    @Future
    private LocalDate birthday;
    private Boolean friendShip;
    private Set<Integer> friends;

    public User() {
    }

    public User(String email, String login, String name, LocalDate birthday) {
        this.email = email;
        this.login = login;
        this.name = name;
        this.birthday = birthday;
        this.friends = new HashSet<>();
    }
}
