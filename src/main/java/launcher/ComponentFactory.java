package launcher;

import controller.AdministratorController;
import controller.EmployeeController;
import controller.LoginController;
import database.DBConnectionFactory;

import repository.account.AccountRepository;
import repository.account.AccountRepositoryMySQL;
import repository.activity.ActivityRepository;
import repository.activity.ActivityRepositoryMySQL;
import repository.client.ClientRepository;
import repository.client.ClientRepositoryMySQL;
import repository.security.RightsRolesRepository;
import repository.security.RightsRolesRepositoryMySQL;
import repository.user.UserRepository;
import repository.user.UserRepositoryMySQL;
import services.account.AccountService;
import services.account.AccountServiceMySQL;
import services.activity.ActivityService;
import services.activity.ActivityServiceMySQL;
import services.client.ClientService;
import services.client.ClientServiceMySQL;
import services.report.GenerateReportsMySQL;
import services.user.authentication.AuthenticationService;
import services.user.authentication.AuthenticationServiceMySQL;
import services.user.management.AdminManagerService;
import services.user.management.AdminManagerServiceMySQL;
import view.AdministratorView;
import view.EmployeeView;
import view.LoginView;

import java.sql.Connection;

public class ComponentFactory {
    private static ComponentFactory instance;

    private final LoginView loginView;
    private final AdministratorView administratorView;
    private final LoginController loginController;
    private final AdministratorController administratorController;
    private final AuthenticationService authenticationService;
    private final UserRepository userRepository;
    private final RightsRolesRepository rightsRolesRepository;
    private final AdminManagerService adminManagerService;
    private final GenerateReportsMySQL generateReportsMySQL;
    private final ClientRepository clientRepository;
    private final ClientService clientService;
    private final ActivityService activityService;
    private final ActivityRepository activityRepository;
    private final EmployeeView employeeManageClientView;
    private final EmployeeController employeeController;
    private final AccountRepository accountRepository;
    private final AccountService accountService;
    private final Connection connection;

    public ComponentFactory(Boolean componentsForTests) {

        connection = new DBConnectionFactory().getConnectionWrapper(componentsForTests).getConnection();
        this.loginView = new LoginView();

        this.clientRepository = new ClientRepositoryMySQL(connection);
        this.clientService = new ClientServiceMySQL(clientRepository);
        this.accountRepository = new AccountRepositoryMySQL(clientRepository, connection);
        this.accountService = new AccountServiceMySQL(accountRepository, clientRepository);
        this.rightsRolesRepository = new RightsRolesRepositoryMySQL(connection);
        this.userRepository = new UserRepositoryMySQL(connection, rightsRolesRepository);
        this.authenticationService = new AuthenticationServiceMySQL(userRepository, rightsRolesRepository);
        this.employeeManageClientView = new EmployeeView();
        this.administratorView = new AdministratorView();
        this.activityRepository = new ActivityRepositoryMySQL(userRepository, connection);
        this.activityService = new ActivityServiceMySQL(activityRepository);
        this.employeeController = new EmployeeController(employeeManageClientView, loginView, clientService, accountService, activityService);
        this.loginController = new LoginController(loginView, authenticationService, employeeManageClientView, administratorView, userRepository);
        this.adminManagerService = new AdminManagerServiceMySQL(userRepository, rightsRolesRepository);
        this.generateReportsMySQL = new GenerateReportsMySQL(activityService);
        this.administratorController = new AdministratorController(administratorView, loginView, adminManagerService, generateReportsMySQL);


    }

    public static ComponentFactory instance(Boolean componentsForTests) {
        if (instance == null) {
            instance = new ComponentFactory(componentsForTests);
        }
        return instance;
    }

    public LoginView getLoginView() {
        return this.loginView;
    }


    public RightsRolesRepository getRightsRolesRepository() {
        return this.rightsRolesRepository;
    }


    public AdminManagerService getAdminManagerService() {
        return this.adminManagerService;
    }

    public AuthenticationService getAuthenticationService() {
        return this.authenticationService;
    }

    public UserRepository getUserRepository() {
        return this.userRepository;
    }

    public ClientRepository getClientRepository() {
        return this.clientRepository;
    }

    public AccountRepository getAccountRepository() {
        return this.accountRepository;
    }

    public AccountService getAccountService() {
        return this.accountService;
    }

    public ClientService getClientService() {
        return this.clientService;
    }

    public ActivityRepository getActivityRepository() {
        return this.activityRepository;
    }
}
