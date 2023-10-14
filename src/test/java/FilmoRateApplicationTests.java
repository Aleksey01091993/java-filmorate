import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import ru.yandex.practicum.filmorate.FilmorateApplication;
import ru.yandex.practicum.filmorate.exeption.DataNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.dao.impl.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.dao.impl.FriendsDaoImpl;
import ru.yandex.practicum.filmorate.storage.dao.impl.LikesDaoImpl;
import ru.yandex.practicum.filmorate.storage.dao.impl.UserDbStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@SpringBootTest(classes = FilmorateApplication.class)
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmoRateApplicationTests {
    private final UserDbStorage userStorage;
    private final FilmDbStorage filmDbStorage;
    private final FriendsDaoImpl friendsDao;
    private final LikesDaoImpl likesDao;

    @Test
    public void testUserAddGetId() throws DataNotFoundException {
        User user = new User("mr@ta.ru", "hjkj", "nam", LocalDate.now());
        User user1 = userStorage.add(user);
        user1.setId(1);
        User user2 = userStorage.getUser(1);
        Assertions.assertEquals(user1, user2);

    }

    @Test
    public void testGetUsers() {
        userStorage.add(new User("mr@ta.ru", "hjkj", "nam", LocalDate.now()));
        userStorage.add(new User("mr@ta.ru", "hjkj", "nam", LocalDate.now()));
        userStorage.add(new User("mr@ta.ru", "hjkj", "nam", LocalDate.now()));
        List<User> users = userStorage.getUsers();
        Assertions.assertEquals(users.size(), 3);
    }

    @Test
    public void testUpdateUser() throws DataNotFoundException {
        userStorage.add(new User("mr@ta.ru", "hjkj", "nam", LocalDate.now()));
        userStorage.add(new User("mr@ta.ru", "hjkj", "nam", LocalDate.now()));
        userStorage.add(new User("mr@ta.ru", "hjkj", "nam", LocalDate.now()));
        User user = new User("mraa@ta.ru", "lex", "lkj", LocalDate.now());
        user.setId(1);
        userStorage.update(user);
        User user1 = userStorage.getUser(1);
        Assertions.assertEquals(user1, user);
    }


    @Test
    public void testFriendsTest() throws DataNotFoundException {
        User user = userStorage.add(new User("1mr@ta.ru", "hjkj", "nam", LocalDate.now()));
        User user1 = userStorage.add(new User("2mr@ta.ru", "hjkj", "nam", LocalDate.now()));
        User user2 = userStorage.add(new User("3mr@ta.ru", "hjkj", "nam", LocalDate.now()));
        user.setId(1);
        user1.setId(2);
        user2.setId(3);
        int userId = 1;
        int userId1 = 2;
        int userId2 = 3;
        friendsDao.addFriend(userId, userId1);
        friendsDao.addFriend(userId1, userId);
        Assertions.assertEquals(user1, friendsDao.friends(userId).get(0));
        Assertions.assertEquals(user, friendsDao.friends(userId1).get(0));
        friendsDao.addFriend(userId, userId2);
        friendsDao.addFriend(userId1, userId2);
        Assertions.assertEquals(user2, friendsDao.friends(userId).get(1));
        Assertions.assertEquals(user2, friendsDao.friends(userId1).get(1));
        Assertions.assertEquals(friendsDao.mutualFriends(userId, userId1).get(0), user2);
        friendsDao.deleteFriend(userId, userId2);
        friendsDao.deleteFriend(userId1, userId2);
        Assertions.assertTrue(friendsDao.mutualFriends(userId, userId1).isEmpty());
    }

    @Test
    public void testFilmAddGetId() {
        Film film = new Film("film", "oiuoiu", LocalDate.now(), 4.56F, 1);
        filmDbStorage.add(film);
        Assertions.assertNotNull(filmDbStorage.getFilm(1));

    }

    @Test
    public void testGetFilms() {
        filmDbStorage.add(new Film("film", "oiuoiu", LocalDate.now(), 4.56F, 1));
        filmDbStorage.add(new Film("film", "oiuoiu", LocalDate.now(), 4.56F, 1));
        filmDbStorage.add(new Film("film", "oiuoiu", LocalDate.now(), 4.56F, 1));
        List<Film> films = filmDbStorage.getFilms();
        Assertions.assertEquals(films.size(), 3);
    }

    @Test
    public void testUpdateFilm() {
        filmDbStorage.add(new Film("film", "oiuoiu", LocalDate.now(), 4.56F, 1));
        filmDbStorage.add(new Film("film", "oiuoiu", LocalDate.now(), 4.56F, 1));
        filmDbStorage.add(new Film("film", "oiuoiu", LocalDate.now(), 4.56F, 1));
        Film film = filmDbStorage.getFilm(1);
        Film filmUpdate = new Film("fiewdwelm", "oiuofrefiu", LocalDate.now(), 5.56F, 2);
        filmUpdate.setId(1);
        filmDbStorage.update(filmUpdate);
        Film film1 = filmDbStorage.getFilm(1);
        Assertions.assertNotEquals(film, film1);
    }

    @Test
    public void testAddDeleteLikeTopFilm() {
        userStorage.add(new User("1mr@ta.ru", "hjkj", "nam", LocalDate.now()));
        userStorage.add(new User("1mr@ta.ru", "hjkj", "nam", LocalDate.now()));
        userStorage.add(new User("1mr@ta.ru", "hjkj", "nam", LocalDate.now()));
        filmDbStorage.add(new Film("film", "oiuoiu", LocalDate.now(), 4.56F, 1));
        filmDbStorage.add(new Film("film", "oiuoiu", LocalDate.now(), 4.56F, 1));
        filmDbStorage.add(new Film("film", "oiuoiu", LocalDate.now(), 4.56F, 1));
        likesDao.addLike(1, 1);
        likesDao.addLike(2, 1);
        likesDao.addLike(2, 3);
        likesDao.addLike(3, 1);
        likesDao.addLike(3, 2);
        likesDao.addLike(3, 3);
        List<Film> topFilms = filmDbStorage.topFilms(3);

    }


}