package services.account;

import model.Account;
import model.Client;
import model.builder.AccountBuilder;
import model.dataTransferObject.AccountDto;
import model.dataTransferObject.ClientDto;
import model.dataTransferObject.PayBillDto;
import model.dataTransferObject.TransferMoneyDto;
import model.validation.AccountValidation;
import model.validation.Notification;
import repository.account.AccountRepository;
import repository.client.ClientRepository;

import javax.swing.*;
import java.time.LocalDate;
import java.util.List;

import static database.Constants.Rights.TRANSFER_MONEY;


public class AccountServiceMySQL implements AccountService {

    private final AccountRepository accountRepository;
    private final ClientRepository clientRepository;

    public AccountServiceMySQL(AccountRepository accountRepository, ClientRepository clientRepository) {
        this.accountRepository = accountRepository;
        this.clientRepository = clientRepository;
    }

    @Override
    public Boolean createAccount(Client client, Account account) {

        Boolean save = accountRepository.save(account);
        Boolean addAccount = accountRepository.addAccountToClient(account, client);

        return save && addAccount;

    }

    @Override
    public Boolean updateAccount(Account account) {
        return accountRepository.updateAccount(account);
    }

    @Override
    public Boolean deleteAccount(Client client, Account account) {
        Boolean aBoolean = accountRepository.deleteAccountFromClient(account);
        Boolean aBoolean1 = accountRepository.deleteAccount(account);
        return aBoolean && aBoolean1;
    }

    @Override
    public List<Account> getAccount(Client client) {
        return accountRepository.findClientAccount(client);
    }

    @Override
    public Notification<Boolean> transferMoney(Account account1, Account account2, Long amount) {

        Notification<Boolean> notification = new Notification<>();
        if (account1 == null || account2 == null) {
            notification.addError("Invalid Account!");
            notification.setResult(false);
            return notification;
        }
        Long newBalanceAccount1 = account1.getAmountOfMoney() - amount;
        Long newBalanceAccount2 = account2.getAmountOfMoney() + amount;

        if (newBalanceAccount1 < 0) {
            notification.addError("Not enough money!");
            notification.setResult(false);
            return notification;
        }

        Account newAccount1 = new AccountBuilder()
                .setId(account1.getId())
                .setAmountOfMoney(newBalanceAccount1)
                .build();

        Account newAccount2 = new AccountBuilder()
                .setId(account2.getId())
                .setAmountOfMoney(newBalanceAccount2)
                .build();
        if (!accountRepository.updateAccount(newAccount2) || !accountRepository.updateAccount(newAccount1)) {
            notification.addError("Error!");
            notification.setResult(false);
        } else {
            notification.setResult(true);
        }


        return notification;


    }

    @Override
    public Notification<Boolean> processBill(String accountNo, Long amount) {
        Account account = accountRepository.findAccountByIdNumber(accountNo);
        Notification<Boolean> notification = new Notification<>();
        if (account == null) {
            notification.addError("Invalid Account");
            notification.setResult(false);
            return notification;
        }
        Long newBalanceAccount = account.getAmountOfMoney() - amount;
        if (newBalanceAccount < 0) {
            notification.addError("Not enough funds!");
            notification.setResult(false);
            return notification;
        }
        Account build = new AccountBuilder()
                .setId(account.getId())
                .setAmountOfMoney(newBalanceAccount)
                .build();
        if (!accountRepository.updateAccount(build)) {
            notification.addError("Error!");
            notification.setResult(false);
            return notification;
        }

        return notification;
    }

    @Override
    public Account getAccountById(Long accountId) {
        return accountRepository.findAccountById(accountId);
    }

    @Override
    public Notification<Account> getAccountByIdNumber(String accountIdNumber) {
        Notification<Account> notification = new Notification<>();

        Account accountByIdNumber = accountRepository.findAccountByIdNumber(accountIdNumber);
        if (accountByIdNumber == null) {
            notification.addError("Invalid Account!");
            notification.setResult(null);
        } else {
            notification.setResult(accountByIdNumber);
        }

        return notification;
    }

    @Override
    public Notification<Boolean> createAccount(ClientDto clientDto, AccountDto accountDto) {
        Notification<Boolean> notification = new Notification<>();

        try {
            Long amountL = Long.parseLong(accountDto.getAmountOfMoney());
            Account newAccount = new AccountBuilder()
                    .setAmountOfMoney(amountL)
                    .setType(accountDto.getType())
                    .setDateCreation(LocalDate.now())
                    .setIdentificationNumber(accountDto.getIdentificationNumber())
                    .build();
            AccountValidation accountValidation = new AccountValidation(newAccount);

            if (!accountValidation.validate()) {

                accountValidation.getErrors().forEach(notification::addError);
                notification.setResult(false);
                return notification;

            } else {

                Client clientFound = clientRepository.findClientByCNP(clientDto.getCNP());
                if (clientFound == null) {
                    notification.setResult(false);
                    notification.addError("Invalid client");
                    return notification;

                } else {
                    notification.setResult(createAccount(clientFound, newAccount));
                }
            }

        } catch (Exception exception) {
            notification.addError("Invalid amount");
            notification.setResult(false);
            return notification;
        }

        return notification;
    }

    @Override
    public Notification<Boolean> updateAccount(AccountDto accountDto) {
        Notification<Boolean> notification = new Notification<>();
        try {

            Long amount = Long.parseLong(accountDto.getAmountOfMoney());
            Notification<Account> notification1 = getAccountByIdNumber(accountDto.getIdentificationNumber());
            if (!notification1.hasErrors()) {
                Account oldAccount = notification1.getResult();
                Account newAccount = new AccountBuilder()
                        .setId(oldAccount.getId())
                        .setIdentificationNumber(accountDto.getIdentificationNumber())
                        .setAmountOfMoney(amount)
                        .setDateCreation(oldAccount.getDateCreation())
                        .setType(oldAccount.getType())
                        .build();

                if (updateAccount(newAccount)) {
                    notification.setResult(true);
                } else {
                    notification.setResult(false);
                    notification.addError("Invalid Operation!");
                    return notification;
                }
            } else {
                notification.addError(notification1.getFormattedErrors());
                notification.setResult(false);
                return notification;

            }
        } catch (Exception exception) {
            notification.addError("Invalid amount!");
            notification.setResult(false);
            return notification;
        }
        return notification;
    }

    @Override
    public Notification<Boolean> deleteAccount(ClientDto clientDto, AccountDto accountDto) {
        Notification<Boolean> notification = new Notification<>();

        Notification<Account> accountNotification = getAccountByIdNumber(accountDto.getIdentificationNumber());
        if (!accountNotification.hasErrors()) {

            Account accountByIdNumber = accountNotification.getResult();
            Client clientByCNP = clientRepository.findClientByCNP(clientDto.getCNP());

            if (clientByCNP != null) {

                if (deleteAccount(clientByCNP, accountByIdNumber)) {

                    notification.setResult(true);

                } else {
                    notification.setResult(false);
                    notification.addError("Invalid Operation!");

                }

            } else {
                notification.addError("Invalid Client!");
                notification.setResult(false);
            }

        } else {
            notification.addError("Invalid Account!");
            notification.setResult(false);
        }
        return notification;
    }

    @Override
    public Notification<Boolean> processBill(PayBillDto payBillDto) {
        Notification<Boolean> notification = new Notification<>();
        try {
            notification = processBill(payBillDto.getAccountFrom(), Long.parseLong(payBillDto.getAmount()));
            if (!notification.hasErrors()) {
                notification.setResult(true);
            } else {
                return notification;

            }
        } catch (Exception exception) {
            notification.addError("Invalid amount!");
            notification.setResult(false);
        }
        return notification;
    }

    @Override
    public Notification<Boolean> transferMoney(TransferMoneyDto transferMoneyDto) {
        Notification<Boolean> notification = new Notification<>();
        try {

            long amount = Long.parseLong(transferMoneyDto.getAmount());

            Notification<Account> accountNotification = getAccountByIdNumber(transferMoneyDto.getAccountFrom());
            Notification<Account> accountNotification1 = getAccountByIdNumber(transferMoneyDto.getAccountTo());
            if (accountNotification.hasErrors() || accountNotification1.hasErrors()) {
                notification.setResult(false);
                notification.addError("Invalid account!");
                return notification;
            } else {
                Account accountF = accountNotification.getResult();
                Account accountT = accountNotification1.getResult();

                Notification<Boolean> notification1 = transferMoney(accountF, accountT, amount);
                if (notification1.hasErrors()) {
                    return notification1;
                }

            }
        } catch (Exception exception) {
            notification.addError("Invalid amount!");
            notification.setResult(false);

        }
        return notification;
    }
}
