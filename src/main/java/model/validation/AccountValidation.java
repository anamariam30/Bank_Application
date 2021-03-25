package model.validation;

import model.Account;
import model.Client;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class AccountValidation {
    private final String DIGIT_REGEX = "^[0-9]+$";
    private Account account;
    private final List<String> errors = new ArrayList<>();

    public AccountValidation(Account account) {
        this.account = account;
    }

    private void validateType(String type) {
        if (type.trim().length() == 0) {
            errors.add("Invalid type");
        }
    }


    private void validateID(String Id) {
        if (!Pattern.compile(DIGIT_REGEX).matcher(Id).matches()) {
            errors.add("Invalid identification number");
        }
    }

    public boolean validate() {

        validateID(account.getIdentificationNumber());
        validateType(account.getType());
        return errors.isEmpty();

    }


    public List<String> getErrors() {
        return errors;
    }

}
