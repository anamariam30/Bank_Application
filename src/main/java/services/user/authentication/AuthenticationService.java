package services.user.authentication;

import model.User;
import model.validation.Notification;

public interface AuthenticationService {
    Notification<User> login(String username, String password);

}
