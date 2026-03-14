package model;

public class PaymentMethod {
    private String paymentMethodId;
    private int isDeleted;

    public PaymentMethod() {}

    public PaymentMethod(String paymentMethodId, int isDeleted) {
        this.paymentMethodId = paymentMethodId;
        this.isDeleted = isDeleted;
    }

    // Getters and Setters...
}