package services.user.management;

import launcher.ComponentFactory;
import model.User;
import model.validation.Notification;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import repository.user.UserRepository;

import java.util.List;

import static org.junit.Assert.*;

public class AdminManagerServiceMySQLTest {
    private static AdminManagerService adminManagerService;
    private static UserRepository userRepository;
    @BeforeClass
    public static void setupClass() {
        ComponentFactory componentFactory = ComponentFactory.instance(true);
        userRepository=componentFactory.getUserRepository();
        adminManagerService=componentFactory.getAdminManagerService();


    }
    @Before
    public void setup() {
        userRepository.removeAll();
    }
    @Test
    public void createEmployee() {
        String username = "User@email.com";
        String password = "Password1!";
        Notification<Boolean> employeeNotification = adminManagerService.createEmployee(username, password);
        Assert.assertTrue(employeeNotification.getResult());
    }

    @Test
    public void deleteEmployee() {
        String username = "User@email.com";
        String password = "Password1!";
        adminManagerService.createEmployee(username, password);
        Notification<Boolean> notification = adminManagerService.deleteEmployee(username);
        Assert.assertTrue(notification.getResult());


    }

    @Test
    public void updateEmployee() {
        String username = "User@email.com";
        String password = "Password1!";
        adminManagerService.createEmployee(username, password);
        Notification<Boolean> notification = adminManagerService.updateEmployee(username, "User1@email.com",
                "newPassword1!", false);
        Assert.assertTrue(notification.getResult());

    }

    @Test
    public void getAllEmployees() {

        String username = "User@email.com";
        String password = "Password1!";
        adminManagerService.createEmployee(username, password);
        String username1 = "User1@email.com";
        String password1 = "Password11!";
        adminManagerService.createEmployee(username1, password1);
        String username2 = "User2@email.com";
        String password2 = "Password12!";
        adminManagerService.createEmployee(username2, password2);

        List<User> employees=adminManagerService.getAllEmployees();

        Assert.assertFalse(employees.isEmpty());

    }
}