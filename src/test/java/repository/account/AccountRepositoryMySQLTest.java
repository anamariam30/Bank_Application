package repository.account;

import launcher.ComponentFactory;
import model.Account;
import model.Client;
import model.User;
import model.builder.AccountBuilder;
import model.builder.ClientBuilder;
import model.builder.UserBuilder;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import repository.activity.ActivityRepository;
import repository.activity.ActivityRepositoryMySQL;
import repository.client.ClientRepository;
import repository.client.ClientRepositoryMySQL;
import repository.security.RightsRolesRepository;
import repository.security.RightsRolesRepositoryMySQL;
import repository.user.UserRepository;
import repository.user.UserRepositoryMySQL;

import java.time.LocalDate;
import java.util.List;

import static database.Constants.Roles.EMPLOYEE;
import static org.junit.Assert.*;

public class AccountRepositoryMySQLTest {

    private static AccountRepository repository;
    private static ClientRepository clientRepository;

    @BeforeClass
    public static void setupClass() {
        ComponentFactory componentFactory = ComponentFactory.instance(true);

        clientRepository = componentFactory.getClientRepository();

        repository = componentFactory.getAccountRepository();
    }

    @Before
    public void setup() {
        repository.removeAll();
        clientRepository.removeAll();
    }


    @Test
    public void save() {
        Account account = new AccountBuilder()
                .setId(1L)
                .setType("Euro")
                .setDateCreation(LocalDate.now())
                .setIdentificationNumber("123")
                .setAmountOfMoney(200L)
                .build();

        assertTrue(repository.save(account));
    }

    @Test
    public void addAccountToClient() {
        String identificationNumber = "123";
        Account account = new AccountBuilder()
                .setType("Euro")
                .setDateCreation(LocalDate.now())
                .setIdentificationNumber(identificationNumber)
                .setAmountOfMoney(200L)
                .build();
        String name = "Client One";
        String address = "Sweet Home Alabama";
        String CNP = "6000130550525";
        String cardNo = "1256364";
        Client client = new ClientBuilder()
                .setName(name)
                .setCardNumber(cardNo)
                .setCNP(CNP)
                .setAddress(address)
                .build();
        clientRepository.save(client);
        Client toBeFoundClient = clientRepository.findClientByName(name);
        repository.save(account);
        Account toBeFoundAccount = repository.findAccountByIdNumber(identificationNumber);
        Assert.assertTrue(repository.addAccountToClient(toBeFoundAccount, toBeFoundClient));

    }

    @Test
    public void deleteAccountFromClient() {
        String identificationNumber = "123";
        Account account = new AccountBuilder()
                .setType("Euro")
                .setDateCreation(LocalDate.now())
                .setIdentificationNumber(identificationNumber)
                .setAmountOfMoney(200L)
                .build();
        String name = "Client One";
        String address = "Sweet Home Alabama";
        String CNP = "6000130550525";
        String cardNo = "1256364";
        Client client = new ClientBuilder()
                .setName(name)
                .setCardNumber(cardNo)
                .setCNP(CNP)
                .setAddress(address)
                .build();
        clientRepository.save(client);
        Client toBeFoundClient = clientRepository.findClientByName(name);
        repository.save(account);
        Account toBeFoundAccount = repository.findAccountByIdNumber(identificationNumber);
        repository.addAccountToClient(account, toBeFoundClient);

        Assert.assertTrue(repository.deleteAccountFromClient(toBeFoundAccount));

        List<Account> clientAccounts = repository.findClientAccount(client);
        Assert.assertTrue(clientAccounts.isEmpty());


    }

    @Test
    public void updateAccount() {
        String identificationNumber = "123";
        long amountOfMoney = 200L;
        Account account = new AccountBuilder()
                .setType("Euro")
                .setDateCreation(LocalDate.now())
                .setIdentificationNumber(identificationNumber)
                .setAmountOfMoney(amountOfMoney)
                .build();
        String name = "Client One";
        String address = "Sweet Home Alabama";
        String CNP = "6000130550525";
        String cardNo = "1256364";
        Client client = new ClientBuilder()
                .setName(name)
                .setCardNumber(cardNo)
                .setCNP(CNP)
                .setAddress(address)
                .build();
        clientRepository.save(client);
        Client toBeFoundClient = clientRepository.findClientByName(name);
        repository.save(account);
        Account toBeFoundAccount = repository.findAccountByIdNumber(identificationNumber);
        repository.addAccountToClient(toBeFoundAccount, toBeFoundClient);

        Long newAmountOfMoney = 1000L;

        Assert.assertTrue(repository.updateAccount(new AccountBuilder()
                .setAmountOfMoney(newAmountOfMoney)
                .setId(toBeFoundAccount.getId())
                .setIdentificationNumber(identificationNumber)
                .setDateCreation(toBeFoundAccount.getDateCreation())
                .setType(toBeFoundAccount.getType())
                .build()));

        Account afterUpdateAccount = repository.findAccountByIdNumber(identificationNumber);
        Long amountAfterUpdate = afterUpdateAccount.getAmountOfMoney();
        Assert.assertTrue(newAmountOfMoney.equals(amountAfterUpdate));

    }

    @Test
    public void deleteAccount() {
        String identificationNumber = "123";
        Account account = new AccountBuilder()
                .setType("Euro")
                .setDateCreation(LocalDate.now())
                .setIdentificationNumber(identificationNumber)
                .setAmountOfMoney(200L)
                .build();

        repository.save(account);
        Account toBeDeleteAccount = repository.findAccountByIdNumber(identificationNumber);
        Assert.assertTrue(repository.deleteAccount(account));
        Assert.assertNull(repository.findAccountByIdNumber(identificationNumber));

    }

    @Test
    public void findAccountById() {
        long notToBeFound = -1L;
        Account notFoundAccount = repository.findAccountById(notToBeFound);
        Assert.assertNull(notFoundAccount);

        String identificationNumber = "123";
        Account account = new AccountBuilder()
                .setType("Euro")
                .setDateCreation(LocalDate.now())
                .setIdentificationNumber(identificationNumber)
                .setAmountOfMoney(200L)
                .build();

        repository.save(account);

        String name = "Client One";
        String address = "Sweet Home Alabama";
        String CNP = "6000130550525";
        String cardNo = "1256364";
        Client client = new ClientBuilder()
                .setName(name)
                .setCardNumber(cardNo)
                .setCNP(CNP)
                .setAddress(address)
                .build();
        clientRepository.save(client);
        Client toBeFoundClient = clientRepository.findClientByName(name);
        Account toBeFoundAccount = repository.findAccountByIdNumber(identificationNumber);
        repository.addAccountToClient(account, toBeFoundClient);

        long toFindId = repository.findClientAccount(client).get(0).getId();
        Account accountToBeFound = repository.findAccountById(toFindId);

        Assert.assertNotNull(accountToBeFound);
        Assert.assertTrue(identificationNumber.equals(accountToBeFound.getIdentificationNumber()));
    }

    @Test
    public void findAccountByIdNumber() {
        String notToBeFound = "-1";
        Account notFoundAccount = repository.findAccountByIdNumber(notToBeFound);
        Assert.assertNull(notFoundAccount);

        String identificationNumber = "123";
        Account account = new AccountBuilder()
                .setType("Euro")
                .setDateCreation(LocalDate.now())
                .setIdentificationNumber(identificationNumber)
                .setAmountOfMoney(200L)
                .build();

        repository.save(account);

        String name = "Client One";
        String address = "Sweet Home Alabama";
        String CNP = "6000130550525";
        String cardNo = "1256364";
        Client client = new ClientBuilder()
                .setName(name)
                .setCardNumber(cardNo)
                .setCNP(CNP)
                .setAddress(address)
                .build();

        clientRepository.save(client);

        Client toBeFoundClient = clientRepository.findClientByName(name);

        Account toBeFoundAccount = repository.findAccountByIdNumber(identificationNumber);

        repository.addAccountToClient(toBeFoundAccount, toBeFoundClient);

        Assert.assertFalse(repository.findClientAccount(client).isEmpty());
        Account accountToBeFound = repository.findClientAccount(client).get(0);
        Assert.assertNotNull(accountToBeFound);
    }

    @Test
    public void findClientAccount() {
        String identificationNumber = "123";
        Account account = new AccountBuilder()
                .setType("Euro")
                .setDateCreation(LocalDate.now())
                .setIdentificationNumber(identificationNumber)
                .setAmountOfMoney(200L)
                .build();

        repository.save(account);

        String name = "Client One";
        String address = "Sweet Home Alabama";
        String CNP = "6000130550525";
        String cardNo = "1256364";
        Client client = new ClientBuilder()
                .setName(name)
                .setCardNumber(cardNo)
                .setCNP(CNP)
                .setAddress(address)
                .build();
        clientRepository.save(client);
        Client toBeFoundClient = clientRepository.findClientByName(name);
        Account toBeFoundAccount = repository.findAccountByIdNumber(identificationNumber);
        repository.addAccountToClient(account, toBeFoundClient);

        Assert.assertNotNull(repository.findClientAccount(client));
        Account accountToBeFound = repository.findClientAccount(client).get(0);
        Assert.assertNotNull(accountToBeFound);

    }
}