package model;

public class Client {
    private Long id;
    private String name;
    private String cardNumber;
    private String CNP;
    private String address;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getCNP() {
        return CNP;
    }

    public void setCNP(String CNP) {
        this.CNP = CNP;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return
                "id= " + id +
                        ",  name='" + name + '\'' +
                        ",  cardNumber= " + cardNumber +
                        ",  personalNumericalCode= " + CNP +
                        ",  address= " + address + "\n";
    }
}
