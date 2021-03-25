package services.client;

import model.Client;
import model.User;
import model.builder.ClientBuilder;
import model.dataTransferObject.ClientDto;
import model.validation.ClientValidation;
import model.validation.Notification;
import repository.activity.ActivityRepository;
import repository.client.ClientRepository;

import java.util.List;

import static services.user.PasswordEncrypt.encodePassword;

public class ClientServiceMySQL implements ClientService {

    private final ClientRepository clientRepositoryMySQL;

    public ClientServiceMySQL(ClientRepository clientRepositoryMySQL) {
        this.clientRepositoryMySQL = clientRepositoryMySQL;

    }

    @Override
    public Notification<Boolean> addClient(Client client) {

        ClientValidation clientValidation = new ClientValidation(client);
        boolean valid = clientValidation.validate(client);
        Notification<Boolean> notification = new Notification<>();
        if (!valid) {
            clientValidation.getErrors().forEach(notification::addError);
            notification.setResult(false);
        } else {
            notification.setResult(clientRepositoryMySQL.save(client));
        }
        return notification;
    }


    @Override
    public Notification<Boolean> updateClient(String CNP, String newName, String newAddress, String newCardNo) {

        Notification<Boolean> notification = new Notification<>();

        Client oldClient = clientRepositoryMySQL.findClientByCNP(CNP);
        if (oldClient == null) {
            notification.addError("Invalid Client");
            notification.setResult(false);
            return notification;
        }

        String notEmptyAddress = oldClient.getAddress();

        String notEmptyCardNo = oldClient.getCardNumber();

        String notEmptyName = oldClient.getName();


        if (!emptyFields(newAddress)) {
            notEmptyAddress = newAddress;
        }
        if (!emptyFields(newName)) {
            notEmptyName = newName;
        }
        if (!emptyFields(newCardNo)) {
            notEmptyCardNo = newCardNo;
        }

        Client newClient = new ClientBuilder()
                .setId(oldClient.getId())
                .setAddress(notEmptyAddress)
                .setName(notEmptyName)
                .setCNP(oldClient.getCNP())
                .setCardNumber(notEmptyCardNo)
                .build();

        ClientValidation clientValidation = new ClientValidation(newClient);
        boolean valid = clientValidation.validate(newClient);

        if (!valid) {

            clientValidation.getErrors().forEach(notification::addError);
            notification.setResult(false);

        } else {

            notification.setResult(clientRepositoryMySQL.updateClient(newClient));

        }

        return notification;
    }

    @Override
    public Notification<Client> getClientByCNP(String CNP) {
        Notification<Client> notification = new Notification<>();
        Client clientByCNP = clientRepositoryMySQL.findClientByCNP(CNP);
        if (clientByCNP == null) {
            notification.setResult(null);
            notification.addError("Invalid Client!");
        } else {
            notification.setResult(clientByCNP);
        }

        return notification;
    }

    @Override
    public Notification<Client> getClient(String clientName) {
        Client clientByName = clientRepositoryMySQL.findClientByName(clientName);
        Notification<Client> notification = new Notification<>();
        if (clientByName == null) {
            notification.setResult(null);
            notification.addError("Invalid Client Name!");

        } else {
            notification.setResult(clientByName);
        }

        return notification;
    }

    @Override
    public List<Client> getAllClients() {
        return clientRepositoryMySQL.findAllClient();
    }

    @Override
    public Notification<Boolean> addClient(ClientDto clientDto) {
        return addClient(new ClientBuilder()
                .setName(clientDto.getName())
                .setCardNumber(clientDto.getCardNumber())
                .setCNP(clientDto.getCNP())
                .setAddress(clientDto.getAddress())
                .build());
    }

    @Override
    public Notification<Boolean> updateClient(ClientDto oldClient, ClientDto newClientDto) {
        return updateClient(oldClient.getCNP(), newClientDto.getName(), newClientDto.getAddress(), newClientDto.getCardNumber());
    }

    private boolean emptyFields(String field) {
        if (field.trim().length() == 0)
            return true;
        return false;

    }

}
