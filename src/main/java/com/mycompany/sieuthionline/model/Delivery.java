package com.mycompany.sieuthionline.model;

import java.sql.Date;

public class Delivery {
    private String deliveryId;
    private String orderId;
    private String employeeId;
    private Date executionDate;
    private String status;
    private int isDeleted;

    public Delivery() {
    }

    public Delivery(String deliveryId, String orderId, String employeeId, Date executionDate, String status, int isDeleted) {
        this.deliveryId = deliveryId;
        this.orderId = orderId;
        this.employeeId = employeeId;
        this.executionDate = executionDate;
        this.status = status;
        this.isDeleted = isDeleted;
    }

    public String getDeliveryId() {
        return deliveryId;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public Date getExecutionDate() {
        return executionDate;
    }

    public String getStatus() {
        return status;
    }

    public int getIsDeleted() {
        return isDeleted;
    }

    public void setDeliveryId(String deliveryId) {
        this.deliveryId = deliveryId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public void setExecutionDate(Date executionDate) {
        this.executionDate = executionDate;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setIsDeleted(int isDeleted) {
        this.isDeleted = isDeleted;
    }


}