package model.builder;

import model.Client;

public class ClientBuilder {
    private final Client client;

    public ClientBuilder() {
        this.client = new Client();
    }

    public ClientBuilder setName(String name) {
        client.setName(name);
        return this;
    }

    public ClientBuilder setCardNumber(String cardNumber) {
        client.setCardNumber(cardNumber);
        return this;
    }

    public ClientBuilder setId(Long id) {
        client.setId(id);
        return this;
    }

    public ClientBuilder setCNP(String CNP) {
        client.setCNP(CNP);
        return this;
    }

    public ClientBuilder setAddress(String address) {
        client.setAddress(address);
        return this;
    }

    public Client build() {
        return client;
    }

}
