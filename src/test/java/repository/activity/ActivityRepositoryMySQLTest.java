package repository.activity;

import launcher.ComponentFactory;
import model.Activity;
import model.User;
import model.builder.ActivityBuilder;
import model.builder.UserBuilder;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import repository.security.RightsRolesRepository;
import repository.security.RightsRolesRepositoryMySQL;
import repository.user.UserRepository;
import repository.user.UserRepositoryMySQL;

import java.time.LocalDate;
import java.util.List;

import static database.Constants.Roles.EMPLOYEE;
import static org.junit.Assert.*;

public class ActivityRepositoryMySQLTest {

    private static UserRepository userRepository;
    private static ActivityRepository repository;
    private static RightsRolesRepository roleRepository;

    @BeforeClass
    public static void setupClass() {
        ComponentFactory componentFactory = ComponentFactory.instance(true);

        userRepository = componentFactory.getUserRepository();
        repository = componentFactory.getActivityRepository();
        roleRepository = componentFactory.getRightsRolesRepository();
    }

    @Before
    public void setup() {
        repository.removeAll();
    }

    @Test
    public void addActivity() {
        String username = "User1@email.com";
        String password = "Password1!";

        User user = new UserBuilder()
                .setId(1L)
                .setUsername(username)
                .setPassword(password)
                .setRole(roleRepository.findRoleByTitle(EMPLOYEE))
                .build();
        Activity activity = new ActivityBuilder().setUser(user).setAction("add_client").setTime(LocalDate.now()).build();
        Assert.assertTrue(repository.addActivity(activity));
    }

    @Test
    public void getActivities() {
        List<Activity> noActivity = repository.getActivities(LocalDate.EPOCH, LocalDate.now());
        Assert.assertTrue(noActivity.isEmpty());
    }
}