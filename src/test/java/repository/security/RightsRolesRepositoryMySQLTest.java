package repository.security;

import launcher.ComponentFactory;
import model.Right;
import model.Role;
import model.User;
import model.builder.UserBuilder;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import repository.user.UserRepository;
import repository.user.UserRepositoryMySQL;

import java.util.List;

import static database.Constants.Rights.UPDATE_ACCOUNT;
import static database.Constants.Rights.UPDATE_CLIENT;
import static database.Constants.Roles.ADMINISTRATOR;
import static database.Constants.Roles.EMPLOYEE;
import static org.junit.Assert.*;

public class RightsRolesRepositoryMySQLTest {

    private static UserRepository userRepository;
    private static RightsRolesRepository repository;

    @BeforeClass
    public static void setupClass() {
        ComponentFactory componentFactory = ComponentFactory.instance(true);

        userRepository = componentFactory.getUserRepository();
        repository = componentFactory.getRightsRolesRepository();
    }

    @Before
    public void setup() {
        userRepository.removeAll();
        repository.removeAll();
        repository.removeAllRoles();
        repository.removeAllRights();
    }


    @Test
    public void findRoleForUser() {
        String username = "User1@email.com";
        String password = "Password1!";
        repository.addRole(EMPLOYEE);
        User user = new UserBuilder()
                .setUsername(username)
                .setPassword(password)
                .setRole(repository.findRoleByTitle(EMPLOYEE))
                .build();
        userRepository.save(user);
        Assert.assertTrue(EMPLOYEE.equals(repository.findRoleForUser(userRepository.getUserByName(username).getId()).getRole()));

    }

    @Test
    public void findRoleByTitle() {
        String username = "User1@email.com";
        String password = "Password1!";
        repository.addRole(EMPLOYEE);
        User user = new UserBuilder()
                .setUsername(username)
                .setPassword(password)
                .setRole(repository.findRoleByTitle(EMPLOYEE))
                .build();
        userRepository.save(user);
        Role findRole = repository.findRoleByTitle("employee");
        Assert.assertNotNull(findRole);


    }

    @Test
    public void addRoleToUser() {
        String username = "User1@email.com";
        String password = "Password1!";
        repository.addRole(EMPLOYEE);
        User user = new UserBuilder()
                .setUsername(username)
                .setPassword(password)
                .setRole(repository.findRoleByTitle(EMPLOYEE))
                .build();
        userRepository.save(user);

        Assert.assertTrue(repository.addRoleToUser(userRepository.getUserByName(username), repository.findRoleByTitle(EMPLOYEE)));
    }

    @Test
    public void updateRoleToUser() {

        repository.addRole(EMPLOYEE);
        repository.addRole(ADMINISTRATOR);
        String username = "User1@email.com";
        String password = "Password1!";
        User user = new UserBuilder()
                .setUsername(username)
                .setPassword(password)
                .setRole(repository.findRoleByTitle(EMPLOYEE))
                .build();
        userRepository.save(user);
        User userByName = userRepository.getUserByName(username);
        repository.addRoleToUser(userByName, repository.findRoleByTitle(EMPLOYEE));
        Assert.assertTrue(repository.updateRoleToUser(userByName));
        Assert.assertTrue((userRepository.getUserByName(username).getRoles().getRole().equals(ADMINISTRATOR)));

    }

    @Test
    public void addRole() {
        Assert.assertTrue(repository.addRole("user"));
    }

    @Test
    public void addRight() {
        Assert.assertTrue(repository.addRight("delete_data"));
    }

    @Test
    public void findRightByTitle() {
        repository.addRight(UPDATE_CLIENT);
        Right right = repository.findRightByTitle("update_client");
        Assert.assertNotNull(right);
    }

    @Test
    public void findRightsForRole() {
        repository.addRole(EMPLOYEE);
        repository.addRight(UPDATE_CLIENT);
        repository.addRight(UPDATE_ACCOUNT);
        repository.addRoleRight(repository.findRoleByTitle(EMPLOYEE).getId(), repository.findRightByTitle(UPDATE_CLIENT).getId());
        repository.addRoleRight(repository.findRoleByTitle(EMPLOYEE).getId(), repository.findRightByTitle(UPDATE_ACCOUNT).getId());
        List<Right> rights = repository.findRightsForRole(repository.findRoleByTitle(EMPLOYEE));
        Assert.assertFalse(rights.isEmpty());
    }

    @Test
    public void addRoleRight() {
        Assert.assertTrue(repository.addRole(EMPLOYEE));
    }
}