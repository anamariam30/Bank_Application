package model.dataTransferObject;

public class AccountDto {
    private String identificationNumber;
    private String type;
    private String amountOfMoney;

    public AccountDto(String identificationNumber, String type, String amountOfMoney) {
        this.identificationNumber = identificationNumber;
        this.type = type;
        this.amountOfMoney = amountOfMoney;
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

    public String getAmountOfMoney() {
        return amountOfMoney;
    }

    public void setAmountOfMoney(String amountOfMoney) {
        this.amountOfMoney = amountOfMoney;
    }
}
