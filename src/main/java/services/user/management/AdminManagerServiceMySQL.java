package services.user.management;

import model.Role;
import model.User;
import model.builder.UserBuilder;
import model.dataTransferObject.UserDto;
import model.validation.Notification;
import model.validation.UserValidation;
import repository.security.RightsRolesRepository;
import repository.user.UserRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


import static database.Constants.Roles.EMPLOYEE;
import static services.user.PasswordEncrypt.encodePassword;

public class AdminManagerServiceMySQL implements AdminManagerService {
    private final UserRepository userRepository;
    private final RightsRolesRepository rightsRolesRepository;

    public AdminManagerServiceMySQL(UserRepository userRepository, RightsRolesRepository rightsRolesRepository) {
        this.userRepository = userRepository;
        this.rightsRolesRepository = rightsRolesRepository;
    }

    @Override
    public Notification<Boolean> createEmployee(String username, String password) {
        Role employeeRole = rightsRolesRepository.findRoleByTitle(EMPLOYEE);

        User user = new UserBuilder()
                .setUsername(username)
                .setPassword(password)
                .setRole(employeeRole)
                .build();

        UserValidation userValidator = new UserValidation(user);
        boolean valid = userValidator.validate();
        Notification<Boolean> notification = new Notification<>();
        if (!valid) {
            userValidator.getErrors().forEach(notification::addError);
            notification.setResult(false);
        } else {
            user.setPassword(encodePassword(password));
            notification.setResult(userRepository.save(user));
        }
        return notification;

    }

    @Override
    public Notification<Boolean> createEmployee(UserDto user) {

        return createEmployee(user.getUsername(), user.getPassword());
    }

    @Override
    public Notification<Boolean> deleteEmployee(String name) {
        Notification<Boolean> notification = new Notification<>();

        User user = userRepository.getUserByName(name);
        if (user == null) {
            notification.addError("Invalid user!");
            notification.setResult(false);
            return notification;
        }

        if (!userRepository.delete(user)) {
            notification.addError("Invalid user!");
            notification.setResult(false);
        } else {
            notification.setResult(true);
        }

        return notification;
    }

    @Override
    public Notification<Boolean> updateEmployee(String username, String newUsername, String newPassword, boolean changeRole) {

        Notification<Boolean> notification = new Notification<>();
        User user = userRepository.getUserByName(username);
        if (user == null) {
            notification.addError("Invalid user!");
            notification.setResult(false);
            return notification;
        }

        String notEmptyPassword = user.getPassword();
        String notEmptyUsername = user.getUsername();

        if (!emptyFields(newPassword)) {
            notEmptyPassword = newPassword;
        }

        if (!emptyFields(newUsername)) {
            notEmptyUsername = newUsername;
        }

        User newUser = new UserBuilder()
                .setUsername(notEmptyUsername)
                .setPassword(notEmptyPassword)
                .setRole(user.getRoles())
                .build();

        UserValidation userValidator = new UserValidation(newUser);
        boolean valid = userValidator.validate();

        if (!valid) {

            userValidator.getErrors().forEach(notification::addError);
            notification.setResult(false);

        } else {

            notification.setResult(userRepository.update(user.getId(), notEmptyUsername, encodePassword(notEmptyPassword)));

        }

        User newUser1 = userRepository.getUserById(user.getId());
        if (changeRole) {
            rightsRolesRepository.updateRoleToUser(newUser1);
        }

        return notification;
    }

    @Override
    public List<User> getAllEmployees() {
        List<User> users = userRepository.getAllUsers();
        List<User> employees = new ArrayList<>();

        for (User user : users) {
            Role roleForUser = rightsRolesRepository.findRoleForUser(user.getId());
            if (roleForUser.getRole().equals(EMPLOYEE)) {
                roleForUser.setRights(rightsRolesRepository.findRightsForRole(roleForUser));
                user.setRoles(rightsRolesRepository.findRoleByTitle(EMPLOYEE));
                employees.add(user);
            }
        }

        return employees;
    }

    @Override
    public Notification<Boolean> updateEmployee(UserDto oldUser, UserDto newUser) {
        return updateEmployee(oldUser.getUsername(), newUser.getUsername(),
                newUser.getPassword(), newUser.getRole());

    }

    private boolean emptyFields(String field) {
        if (field.trim().length() == 0)
            return true;
        return false;

    }
}
