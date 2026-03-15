package model;

public class OnSitePickup {
    private String deliveryId;
    private String counterPosition;
    private int isDeleted;

    public OnSitePickup() {}

    public OnSitePickup(String deliveryId, String counterPosition, int isDeleted) {
        this.deliveryId = deliveryId;
        this.counterPosition = counterPosition;
        this.isDeleted = isDeleted;
    }

    // Getters and Setters...
}