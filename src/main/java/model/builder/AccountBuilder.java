package model.builder;

import model.Account;
import model.Role;
import model.User;

import java.time.LocalDate;

public class AccountBuilder {
    private final Account account;

    public AccountBuilder() {
        this.account = new Account();
    }


    public AccountBuilder setIdentificationNumber(String identificationNumber) {
        account.setIdentificationNumber(identificationNumber);
        return this;
    }

    public AccountBuilder setType(String type) {
        account.setType(type);
        return this;
    }

    public AccountBuilder setAmountOfMoney(Long amountOfMoney) {
        account.setAmountOfMoney(amountOfMoney);
        return this;
    }

    public AccountBuilder setId(Long accountId) {
        account.setId(accountId);
        return this;
    }

    public AccountBuilder setDateCreation(LocalDate dateCreation) {
        account.setDateCreation(dateCreation);
        return this;
    }


    public Account build() {
        return account;
    }


}
