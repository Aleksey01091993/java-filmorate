import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import ru.yandex.practicum.filmorate.FilmorateApplication;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.impl.UserDbStorage;

import java.time.LocalDate;
import java.util.List;


@SpringBootTest(classes = FilmorateApplication.class)
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmoRateApplicationTests {
    private final UserDbStorage userStorage;

    @Test
    public void testUserAddGetId() {
        User user = new User("mr@ta.ru", "hjkj", "nam", LocalDate.now());
        User user1 = userStorage.add(user);
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
    public void testUpdateUser() {
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
    public void testFriendsTest() {
        User user = userStorage.add(new User("1mr@ta.ru", "hjkj", "nam", LocalDate.now()));
        User user1 = userStorage.add(new User("2mr@ta.ru", "hjkj", "nam", LocalDate.now()));
        User user2 = userStorage.add(new User("3mr@ta.ru", "hjkj", "nam", LocalDate.now()));
        int userId = user.getId();
        int userId1 = user1.getId();
        int userId2 = user2.getId();
        userStorage.addFriend(userId, userId1);
        userStorage.addFriend(userId1, userId);
        Assertions.assertEquals(user1, userStorage.friends(userId).get(0));
        Assertions.assertEquals(user, userStorage.friends(userId1).get(0));
        userStorage.addFriend(userId, userId2);
        userStorage.addFriend(userId1, userId2);
        Assertions.assertEquals(user2, userStorage.friends(userId).get(1));
        Assertions.assertEquals(user2, userStorage.friends(userId1).get(1));
        Assertions.assertEquals(userStorage.mutualFriends(userId, userId1).get(0), user2);
        userStorage.deleteFriend(userId, userId2);
        userStorage.deleteFriend(userId1, userId2);
        System.out.println(userStorage.mutualFriends(userId, userId1));
        Assertions.assertTrue(userStorage.mutualFriends(userId, userId1).isEmpty());
    }
}