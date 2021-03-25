package repository.client;

import model.Client;

import java.util.List;

public interface ClientRepository {

    Boolean save(Client client);

    Boolean updateClient(Client client);

    Client findClientByName(String name);

    Client findClientById(Long Id);

    List<Client> findAllClient();

    Boolean removeAll();

    Client findClientByCNP(String cnp);
}
