package services.client;

import launcher.ComponentFactory;
import model.Client;
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
import services.account.AccountService;
import services.account.AccountServiceMySQL;

import java.util.List;

import static org.junit.Assert.*;

public class ClientServiceMySQLTest {

    private static AccountRepository accountRepository;
    private static ClientRepository clientRepository;

    private static ClientService clientService;

    @BeforeClass
    public static void setupClass() {
        ComponentFactory componentFactory = ComponentFactory.instance(true);
        clientRepository = componentFactory.getClientRepository();
        clientService=componentFactory.getClientService();
    }

    @Before
    public void setup() {
        clientRepository.removeAll();
    }

    @Test
    public void addClient() {
        Client client = new ClientBuilder()
                .setCNP("1234567891478")
                .setName("Client One")
                .setAddress("Home")
                .setCardNumber("1234")
                .build();

        Notification<Boolean> notification = clientService.addClient(client);
        Assert.assertFalse(notification.hasErrors());


    }

    @Test
    public void updateClient() {
        Client client = new ClientBuilder()
                .setCNP("1234567891478")
                .setName("Client One")
                .setAddress("Home")
                .setCardNumber("1234")
                .build();

        clientService.addClient(client);

        Notification<Boolean> notification = clientService.updateClient(client.getCNP(), client.getName(), "New Home", client.getCardNumber());
        Assert.assertFalse(notification.hasErrors());

    }

    @Test
    public void getClientByCNP() {
        String cnp = "1234567891478";

        Client client = new ClientBuilder()
                .setCNP(cnp)
                .setName("Client One")
                .setAddress("Home")
                .setCardNumber("1234")
                .build();

        clientService.addClient(client);

        Notification<Client> clientNotification = clientService.getClientByCNP(cnp);
        Client client1=  clientNotification.getResult();
        Assert.assertNotNull(client1);
        Assert.assertFalse(clientNotification.hasErrors());

    }

    @Test
    public void getClient() {
        String cnp = "1234567891478";

        String name = "Client One";
        Client client = new ClientBuilder()
                .setCNP(cnp)
                .setName(name)
                .setAddress("Home")
                .setCardNumber("1234")
                .build();

        clientService.addClient(client);

        Notification<Client> clientNotification = clientService.getClient(name);
        Client client1=  clientNotification.getResult();
        Assert.assertNotNull(client1);
        Assert.assertFalse(clientNotification.hasErrors());


    }

    @Test
    public void getAllClients() {
        String cnp = "1234567891478";

        String name = "Client One";
        Client client = new ClientBuilder()
                .setCNP(cnp)
                .setName(name)
                .setAddress("Home")
                .setCardNumber("1234")
                .build();

        clientService.addClient(client);

        Client client2 = new ClientBuilder()
                .setCNP("1010101010101")
                .setName("Client Two")
                .setAddress("Home2 ")
                .setCardNumber("1235")
                .build();

        clientService.addClient(client);
        clientService.addClient(client2);
        List<Client> clients=clientService.getAllClients();
        Assert.assertFalse(clients.isEmpty());


    }
}