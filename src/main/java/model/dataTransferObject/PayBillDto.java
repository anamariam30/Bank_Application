package model.dataTransferObject;

public class PayBillDto {
    private String accountFrom;
    private String amount;

    public PayBillDto(String accountFrom, String amount) {
        this.accountFrom = accountFrom;
        this.amount = amount;
    }

    public String getAccountFrom() {
        return accountFrom;
    }

    public void setAccountFrom(String accountFrom) {
        this.accountFrom = accountFrom;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
