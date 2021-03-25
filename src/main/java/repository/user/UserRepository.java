package repository.user;

import model.User;
import model.validation.Notification;

import java.util.List;

public interface UserRepository {
    Notification<User> findByUsernameAndPassword(String username, String encodePassword);

    Boolean save(User user);

    Boolean delete(User user);

    User getUserById(long userId);

    User getUserByName(String username);

    List<User> getAllUsers();

    Boolean update(Long userId, String username, String password);

    Boolean removeAll();

}
