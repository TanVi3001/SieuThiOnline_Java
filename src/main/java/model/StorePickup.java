package model;

import java.sql.Timestamp;

public class StorePickup {
    private String deliveryId;
    private String lockerId;
    private Timestamp pickupAppointment;
    private int isDeleted;

    public StorePickup() {}

    public StorePickup(String deliveryId, String lockerId, Timestamp pickupAppointment, int isDeleted) {
        this.deliveryId = deliveryId;
        this.lockerId = lockerId;
        this.pickupAppointment = pickupAppointment;
        this.isDeleted = isDeleted;
    }

    // Getters and Setters...
}