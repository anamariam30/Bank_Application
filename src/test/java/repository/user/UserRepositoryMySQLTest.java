package repository.user;

import launcher.ComponentFactory;
import model.User;
import model.builder.UserBuilder;
import model.validation.Notification;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import repository.security.RightsRolesRepository;
import repository.security.RightsRolesRepositoryMySQL;

import java.time.LocalDate;
import java.util.List;

import static database.Constants.Roles.EMPLOYEE;
import static org.junit.Assert.*;

public class UserRepositoryMySQLTest {

    private static UserRepository repository;
    private static RightsRolesRepository roleRepository;

    @BeforeClass
    public static void setupClass() {
        ComponentFactory componentFactory = ComponentFactory.instance(true);

        repository = componentFactory.getUserRepository();
        roleRepository = componentFactory.getRightsRolesRepository();
    }

    @Before
    public void setup() {
        repository.removeAll();
    }

    @Test
    public void findByUsernameAndPassword() {
        String username = "User1@email.com";
        String password = "Password1!";
        User user = new UserBuilder().setUsername(username).setPassword(password).setRole(roleRepository.findRoleByTitle(EMPLOYEE)).build();
        repository.save(user);
        Notification<User> notification = repository.findByUsernameAndPassword(username, password);
        Assert.assertFalse(notification.hasErrors());
    }

    @Test
    public void save() {
        String username = "User1@email.com";
        String password = "Password1!";
        User user = new UserBuilder()
                .setUsername(username)
                .setPassword(password)
                .setRole(roleRepository.findRoleByTitle(EMPLOYEE))
                .build();
        assertTrue(repository.save(user));
    }

    @Test
    public void delete() {
        String username = "User1@email.com";
        String password = "Password1!";
        User user = new UserBuilder()
                .setUsername(username)
                .setPassword(password)
                .setRole(roleRepository.findRoleByTitle(EMPLOYEE))
                .build();
        repository.save(user);
        assertTrue(repository.delete(user));

    }

    @Test
    public void getUserById() {
        long notToBeFound = -1L;
        User notFoundUser = repository.getUserById(notToBeFound);
        Assert.assertNull(notFoundUser);

        String username = "User1@email.com";
        String password = "Password1!";
        User user = new UserBuilder()
                .setUsername(username)
                .setPassword(password)
                .setRole(roleRepository.findRoleByTitle(EMPLOYEE))
                .build();
        repository.save(user);


        long toFindId = repository.getAllUsers().get(0).getId();
        User foundUser = repository.getUserById(toFindId);

        Assert.assertNotNull(foundUser);
        Assert.assertEquals(username, foundUser.getUsername());
    }

    @Test
    public void getUserByName() {
        String notToBeFound = "NotAName";
        User notFoundUser = repository.getUserByName(notToBeFound);
        Assert.assertNull(notFoundUser);

        String username = "User1@email.com";
        String password = "Password1!";
        User user = new UserBuilder()
                .setUsername(username)
                .setPassword(password)
                .setRole(roleRepository.findRoleByTitle(EMPLOYEE))
                .build();
        repository.save(user);


        String toFindName = repository.getAllUsers().get(0).getUsername();
        User foundUser = repository.getUserByName(toFindName);

        Assert.assertNotNull(foundUser);
        Assert.assertEquals(username, foundUser.getUsername());

    }

    @Test
    public void getAllUsers() {
        List<User> noUser = repository.getAllUsers();
        Assert.assertTrue(noUser.isEmpty());
    }

    @Test
    public void update() {
        String username = "User1@email.com";
        String password = "Password1!";
        User user = new UserBuilder()
                .setUsername(username)
                .setPassword(password)
                .setRole(roleRepository.findRoleByTitle(EMPLOYEE))
                .build();
        repository.save(user);
        Long oldUser = repository.getUserByName(username).getId();
        String newUsername = "NewUSer1@email.com";
        Assert.assertNull(repository.getUserByName(newUsername));
        Assert.assertTrue(repository.update(user.getId(), newUsername, user.getPassword()));

        Long newUser = repository.getUserByName(newUsername).getId();

        Assert.assertTrue(newUser.equals(oldUser));

    }
}