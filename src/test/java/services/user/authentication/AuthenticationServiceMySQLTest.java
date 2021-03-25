package services.user.authentication;

import launcher.ComponentFactory;
import model.Role;
import model.User;
import model.builder.UserBuilder;
import model.validation.Notification;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import repository.account.AccountRepository;
import repository.account.AccountRepositoryMySQL;
import repository.client.ClientRepository;
import repository.client.ClientRepositoryMySQL;
import repository.security.RightsRolesRepository;
import repository.user.UserRepository;
import services.account.AccountService;
import services.account.AccountServiceMySQL;
import services.client.ClientService;
import services.client.ClientServiceMySQL;
import services.user.management.AdminManagerService;
import services.user.management.AdminManagerServiceMySQL;

import static org.junit.Assert.*;

public class AuthenticationServiceMySQLTest {

    private static UserRepository userRepository;

    private static AdminManagerService adminManagerService;
    private static AuthenticationService authenticationService;

    @BeforeClass
    public static void setupClass() {
        ComponentFactory componentFactory = ComponentFactory.instance(true);


        userRepository=componentFactory.getUserRepository();
        adminManagerService=componentFactory.getAdminManagerService();
        authenticationService=componentFactory.getAuthenticationService();
    }

    @Before
    public void setup() {
        userRepository.removeAll();
    }

    @Test
    public void login() {
        String username = "User@email.com";

        String password = "Password1!";
        adminManagerService.createEmployee(username,password);

        Notification<User> userNotification = authenticationService.login(username, password);
        Assert.assertFalse(userNotification.hasErrors());
        Assert.assertNotNull(userNotification.getResult());


    }
}