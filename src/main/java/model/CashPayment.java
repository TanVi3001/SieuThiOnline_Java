package model;

public class CashPayment {
    private String paymentMethodId;
    private int isDeleted;

    public CashPayment() {}

    public CashPayment(String paymentMethodId, int isDeleted) {
        this.paymentMethodId = paymentMethodId;
        this.isDeleted = isDeleted;
    }

    // Getters and Setters...
}