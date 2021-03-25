package model.validation;

import model.Client;
import model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class ClientValidation {
    private final static String NAME_VALIDATION_REGEX = "^[a-zA-Z ,.'-]+$";
    private final static String VALIDATE_CNP_REGEX = "^(\\d{13})?$";
    private final String DIGIT_REGEX = "^[0-9]+$";
    private Client client;
    private final List<String> errors = new ArrayList<>();

    public ClientValidation(Client client) {

        this.client = client;
    }

    private void validateName(String name) {
        if (!Pattern.compile(NAME_VALIDATION_REGEX).matcher(name).matches()) {
            errors.add("Invalid name");
        }

    }

    public void validateCNP(String CNP) {
        if (!Pattern.compile(VALIDATE_CNP_REGEX).matcher(CNP).matches()) {
            errors.add("Invalid CNP");
        }

    }


    public boolean validate(Client client) {

        validateName(client.getName());
        validateCNP(client.getCNP());
        validateAddress(client.getAddress());
        validateCardNo(client.getCardNumber());

        return errors.isEmpty();

    }

    private void validateCardNo(String cardNumber) {
        if (!Pattern.compile(DIGIT_REGEX).matcher(cardNumber).matches()) {
            errors.add("Invalid cardNo!");
        }

    }

    private void validateAddress(String address) {
        if (emptyFields(address)) {
            errors.add("Invalid address!");
        }

    }


    private boolean emptyFields(String field) {
        if (field.trim().length() == 0)
            return true;
        return false;

    }

    public List<String> getErrors() {

        return errors;
    }
}
