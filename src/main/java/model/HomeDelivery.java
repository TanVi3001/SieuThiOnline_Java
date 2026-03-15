package model;

public class HomeDelivery {
    private String deliveryId;
    private String deliveryAddress;
    private double shippingFee;
    private String recipientPhone;
    private int isDeleted;

    public HomeDelivery() {}

    public HomeDelivery(String deliveryId, String deliveryAddress, double shippingFee, String recipientPhone, int isDeleted) {
        this.deliveryId = deliveryId;
        this.deliveryAddress = deliveryAddress;
        this.shippingFee = shippingFee;
        this.recipientPhone = recipientPhone;
        this.isDeleted = isDeleted;
    }

    // Getters and Setters...
}