package services.account;

import launcher.ComponentFactory;
import model.Account;
import model.Client;
import model.builder.AccountBuilder;
import model.builder.ClientBuilder;
import model.validation.Notification;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import repository.account.AccountRepository;
import repository.account.AccountRepositoryMySQL;
import repository.client.ClientRepository;
import repository.client.ClientRepositoryMySQL;
import services.client.ClientService;
import services.client.ClientServiceMySQL;

import java.time.LocalDate;
import java.util.List;

import static org.junit.Assert.*;

public class AccountServiceMySQLTest {

    private static AccountRepository accountRepository;
    private static ClientRepository clientRepository;

    private static AccountService accountService;
    private static ClientService clientService;

    @BeforeClass
    public static void setupClass() {
        ComponentFactory componentFactory = ComponentFactory.instance(true);

        clientRepository = componentFactory.getClientRepository();
        accountRepository = componentFactory.getAccountRepository();
        accountService=componentFactory.getAccountService();
        clientService=componentFactory.getClientService();
    }

    @Before
    public void setup() {
        clientRepository.removeAll();
    }

    @Test
    public void createAccount() {

        Client client = new ClientBuilder()
                .setCNP("1234567891478")
                .setName("Client One")
                .setAddress("Home")
                .setCardNumber("1234")
                .build();

        clientService.addClient(client);
        Account account = new AccountBuilder()
                .setDateCreation(LocalDate.now())
                .setIdentificationNumber("123")
                .setType("EURO")
                .setAmountOfMoney(100L)
                .build();
        Assert.assertTrue(accountService.createAccount(client, account));
    }

    @Test
    public void updateAccount() {
        Client client = new ClientBuilder()
                .setCNP("1234567891478")
                .setName("Client One")
                .setAddress("Home")
                .setCardNumber("1234")
                .build();

        clientService.addClient(client);
        Account account = new AccountBuilder()
                .setDateCreation(LocalDate.now())
                .setIdentificationNumber("123")
                .setType("EURO")
                .setAmountOfMoney(100L)
                .build();
        accountService.createAccount(client, account);

        account.setAmountOfMoney(0L);

        Assert.assertTrue(accountService.updateAccount(account));


    }

    @Test
    public void deleteAccount() {
        Client client = new ClientBuilder()
                .setCNP("1234567891478")
                .setName("Client One")
                .setAddress("Home")
                .setCardNumber("1234")
                .build();

        clientService.addClient(client);
        Account account = new AccountBuilder()
                .setDateCreation(LocalDate.now())
                .setIdentificationNumber("123")
                .setType("EURO")
                .setAmountOfMoney(100L)
                .build();
        accountService.createAccount(client, account);

        Assert.assertTrue(accountService.deleteAccount(client,account));
    }

    @Test
    public void getAccount() {
        Client client = new ClientBuilder()
                .setCNP("1234567891478")
                .setName("Client One")
                .setAddress("Home")
                .setCardNumber("1234")
                .build();

        clientService.addClient(client);
        Account account1 = new AccountBuilder()
                .setDateCreation(LocalDate.now())
                .setIdentificationNumber("123")
                .setType("EURO")
                .setAmountOfMoney(100L)
                .build();

        Account account2 = new AccountBuilder()
                .setDateCreation(LocalDate.now())
                .setIdentificationNumber("124")
                .setType("EURO")
                .setAmountOfMoney(10L)
                .build();


        accountService.createAccount(client, account1);
        accountService.createAccount(client, account2);

        List<Account> accountList= accountService.getAccount(client);
        Assert.assertFalse(accountList.isEmpty());
    }

    @Test
    public void transferMoney() {
        Client client = new ClientBuilder()
                .setCNP("1234567891478")
                .setName("Client One")
                .setAddress("Home")
                .setCardNumber("1234")
                .build();

        clientService.addClient(client);
        Account account1 = new AccountBuilder()
                .setDateCreation(LocalDate.now())
                .setIdentificationNumber("123")
                .setType("EURO")
                .setAmountOfMoney(100L)
                .build();

        Account account2 = new AccountBuilder()
                .setDateCreation(LocalDate.now())
                .setIdentificationNumber("124")
                .setType("EURO")
                .setAmountOfMoney(10L)
                .build();


        accountService.createAccount(client, account1);
        accountService.createAccount(client, account2);

        Long amount=1L;

        Notification<Boolean> notification = accountService.transferMoney(account1, account2, amount);
        Assert.assertFalse(notification.hasErrors());

    }

    @Test
    public void processBill() {
        Client client = new ClientBuilder()
                .setCNP("1234567891478")
                .setName("Client One")
                .setAddress("Home")
                .setCardNumber("1234")
                .build();

        clientService.addClient(client);
        String identificationNumber = "123";
        Account account1 = new AccountBuilder()
                .setDateCreation(LocalDate.now())
                .setIdentificationNumber(identificationNumber)
                .setType("EURO")
                .setAmountOfMoney(100L)
                .build();



        accountService.createAccount(client, account1);

        Long amount=1L;

        Notification<Boolean> notification =accountService.processBill(identificationNumber,amount);
        Assert.assertFalse(notification.hasErrors());

    }


    @Test
    public void getAccountByIdNumber() {
        Client client = new ClientBuilder()
                .setCNP("1234567891478")
                .setName("Client One")
                .setAddress("Home")
                .setCardNumber("1234")
                .build();

        clientService.addClient(client);
        String identificationNumber = "123";
        Account account1 = new AccountBuilder()
                .setDateCreation(LocalDate.now())
                .setIdentificationNumber(identificationNumber)
                .setType("EURO")
                .setAmountOfMoney(100L)
                .build();



        accountService.createAccount(client, account1);

        Account account=accountService.getAccountByIdNumber(identificationNumber).getResult();

        Account accountById = accountService.getAccountById(account.getId());
        Assert.assertTrue(accountById.getIdentificationNumber().equals(account.getIdentificationNumber()));
    }
}