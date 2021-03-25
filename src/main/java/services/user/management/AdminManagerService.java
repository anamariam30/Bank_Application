package services.user.management;

import model.User;
import model.dataTransferObject.UserDto;
import model.validation.Notification;

import java.util.List;

public interface AdminManagerService {
    Notification<Boolean> createEmployee(String username, String Password);

    Notification<Boolean> createEmployee(UserDto user);

    Notification<Boolean> deleteEmployee(String name);

    Notification<Boolean> updateEmployee(String username, String newUsername, String newPassword, boolean changeRole);

    List<User> getAllEmployees();

    Notification<Boolean> updateEmployee(UserDto oldUser, UserDto newUser);
}
