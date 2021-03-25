package controller;

import model.User;
import model.validation.Notification;
//import services.user.AuthenticationService;
import repository.user.UserRepository;
import services.user.authentication.AuthenticationService;
import view.AdministratorView;
import view.EmployeeView;
import view.LoginView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static database.Constants.Roles.ADMINISTRATOR;
import static database.Constants.Roles.EMPLOYEE;

public class LoginController {

    private final LoginView loginView;
    private final AuthenticationService authenticationService;
    private final EmployeeView employeeManageClientView;
    private final AdministratorView administratorView;
    private final UserRepository userRepository;

    public LoginController(LoginView loginView, AuthenticationService authenticationService, EmployeeView employeeManageClientView, AdministratorView administratorView, UserRepository userRepository) {
        this.loginView = loginView;
        this.authenticationService = authenticationService;
        this.employeeManageClientView = employeeManageClientView;
        this.administratorView = administratorView;
        this.userRepository = userRepository;
        loginView.setLoginButtonActionListener(new LoginButtonListener());
    }

    private class LoginButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            String username = loginView.getUsername();
            String password = loginView.getPassword();
            Notification<User> loginNotification = authenticationService.login(username, password);


            if (loginNotification.hasErrors()) {
                JOptionPane.showMessageDialog(loginView.getContentPane(), loginNotification.getFormattedErrors());
            } else {
                JOptionPane.showMessageDialog(loginView.getContentPane(), "Login successful!");
                User employee = userRepository.getUserByName(username);
                employeeManageClientView.setEmployee(employee);
                if (employee.getRoles().getRole().equals(ADMINISTRATOR)) {
                    administratorView.setVisible(true);
                    loginView.setVisible(false);
                } else {
                    if (employee.getRoles().getRole().equals(EMPLOYEE)) {
                        employeeManageClientView.setVisible(true);
                        loginView.setVisible(false);
                    }
                }
            }

        }
    }


}
