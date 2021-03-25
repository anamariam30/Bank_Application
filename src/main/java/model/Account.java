package model;

import java.time.LocalDate;

public class Account {
    private Long id;
    private String identificationNumber;
    private String type;
    private Long amountOfMoney;
    private LocalDate dateCreation;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIdentificationNumber() {
        return identificationNumber;
    }

    public void setIdentificationNumber(String identificationNumber) {
        this.identificationNumber = identificationNumber;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getAmountOfMoney() {
        return amountOfMoney;
    }

    public void setAmountOfMoney(Long amountOfMoney) {
        this.amountOfMoney = amountOfMoney;
    }

    public LocalDate getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(LocalDate dateCreation) {
        this.dateCreation = dateCreation;
    }

    @Override
    public String

    toString() {
        return "id=" + id +
                ", identificationNumber=" + identificationNumber +
                ", type='" + type + '\'' +
                ", amountOfMoney=" + amountOfMoney +
                ", dateCreation=" + dateCreation + "\n";
    }
}
