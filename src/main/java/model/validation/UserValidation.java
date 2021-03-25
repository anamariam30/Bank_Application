package model.validation;

import model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserValidation {

    private static final String EMAIL_VALIDATION_REGEX = "^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";
    private static final int MIN_PASSWORD_LENGTH = 8;
    private final User user;
    private final List<String> errors = new ArrayList<>();

    public UserValidation(User user) {

        this.user = user;
    }


    public boolean validate() {

        validateUsername(user.getUsername());
        validatePassword(user.getPassword());

        return errors.isEmpty();

    }

    private void validatePassword(String password) {
        if (password.length() < MIN_PASSWORD_LENGTH) {
            errors.add("Password too short! Must be at least " + MIN_PASSWORD_LENGTH + "  in length.");
        }
        if (!containsSpecialCharacter(password)) {
            errors.add("Password must contain at least one special character!");
        }
        if (!containsDigit(password)) {
            errors.add("Password must contain at least one number!");
        }

    }

    private boolean containsDigit(String password) {

        if (password != null && !password.isEmpty()) {
            for (char c : password.toCharArray()) {
                if (Character.isDigit(c)) {
                    return true;
                }
            }
        }
        return false;

    }

    private boolean containsSpecialCharacter(String password) {

        if (password == null || password.trim().isEmpty()) {
            return false;
        }
        Pattern p = Pattern.compile("[^A-Za-z0-9]");
        Matcher m = p.matcher(password);
        return m.find();
    }

    private void validateUsername(String username) {
        if (!Pattern.compile(EMAIL_VALIDATION_REGEX).matcher(username).matches()) {
            errors.add("Invalid username, must be an email!");
        }

    }

    public List<String> getErrors() {

        return errors;
    }
}
