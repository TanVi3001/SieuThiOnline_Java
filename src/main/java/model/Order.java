package model;

import java.sql.Date;

public class Order {
    private String orderId;
    private String customerId;
    private String employeeId;
    private Date orderDate;
    private double totalAmount;
    private String status;

    public Order() {}

    public Order(String orderId, String customerId, String employeeId, Date orderDate, double totalAmount, String status) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.employeeId = employeeId;
        this.orderDate = orderDate;
        this.totalAmount = totalAmount;
        this.status = status;
    }

    // Các hàm Getter mà OrdersSql đang kêu cứu đây:
    public String getOrderId() { return orderId; }
    public String getCustomerId() { return customerId; }
    public String getEmployeeId() { return employeeId; }
    public Date getOrderDate() { return orderDate; }
    public double getTotalAmount() { return totalAmount; }
    public String getStatus() { return status; }

    // (Có thể thêm Setter nếu cần...)
}