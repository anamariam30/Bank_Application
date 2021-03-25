package repository.client;

import launcher.ComponentFactory;
import model.Client;
import model.User;
import model.builder.ClientBuilder;
import model.builder.UserBuilder;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import repository.security.RightsRolesRepository;
import repository.security.RightsRolesRepositoryMySQL;
import repository.user.UserRepository;
import repository.user.UserRepositoryMySQL;

import java.util.List;

import static database.Constants.Roles.EMPLOYEE;
import static org.junit.Assert.*;

public class ClientRepositoryMySQLTest {

    private static ClientRepository repository;


    @BeforeClass
    public static void setupClass() {
        ComponentFactory componentFactory = ComponentFactory.instance(true);

        repository = componentFactory.getClientRepository();
    }

    @Before
    public void setup() {
        repository.removeAll();
    }

    @Test
    public void save() {
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
        assertTrue(repository.save(client));
    }

    @Test
    public void updateClient() {
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
        repository.save(client);
        Long oldClient = repository.findClientByName(name).getId();
        String newName = "Client Two";
        Client clientByName = repository.findClientByName(newName);
        Assert.assertNull(clientByName);
        client.setName(newName);
        Assert.assertTrue(repository.updateClient(client));

        Long newClient = repository.findClientByName(newName).getId();

        Assert.assertTrue(newClient.equals(oldClient));
    }

    @Test
    public void findClientByName() {

        String notToBeFound = "NotAName";
        Client notFoundClient = repository.findClientByName(notToBeFound);
        Assert.assertNull(notFoundClient);

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
        repository.save(client);

        String toFindName = repository.findAllClient().get(0).getName();
        Client foundClient = repository.findClientByName(toFindName);

        Assert.assertNotNull(foundClient);
        Assert.assertEquals(name, foundClient.getName());
    }

    @Test
    public void findClientById() {
        long notToBeFound = -1L;
        Client notFoundClient = repository.findClientById(notToBeFound);
        Assert.assertNull(notFoundClient);

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
        repository.save(client);


        long toFindId = repository.findAllClient().get(0).getId();
        Client clientToBeFound = repository.findClientById(toFindId);

        Assert.assertNotNull(clientToBeFound);
        Assert.assertEquals(name, clientToBeFound.getName());
    }

    @Test
    public void findAllClient() {
        List<Client> noClient = repository.findAllClient();
        Assert.assertTrue(noClient.isEmpty());
    }
}