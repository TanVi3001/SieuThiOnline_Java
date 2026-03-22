package model.order;

import java.sql.Date;

public class Order {
    private String orderId;
    private String customerId;
    private String employeeId;
    private Date orderDate;
    private double totalAmount;
    private String status;
    private boolean isDeleted;

    public Order() {}

    public Order(String orderId, String customerId, String employeeId,
                 Date orderDate, double totalAmount, String status, boolean isDeleted) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.employeeId = employeeId;
        this.orderDate = orderDate;
        this.totalAmount = totalAmount;
        this.status = status;
        this.isDeleted = isDeleted;  
    }
    
    // THÊM MỚI: Constructor 6 tham số (Tự gán isDeleted = false)
    public Order(String id, String cusId, String empId, Date date, double total, String note) {
        this(id, cusId, empId, date, total, note, false); // Gọi lại constructor trên
    }

    // Các hàm Getter mà OrdersSql đang kêu cứu đây:
    public String getOrderId() { return orderId; }
    public String getCustomerId() { return customerId; }
    public String getEmployeeId() { return employeeId; }
    public Date getOrderDate() { return orderDate; }
    public double getTotalAmount() { return totalAmount; }
    public String getStatus() { return status; }
    public boolean isDeleted() { return isDeleted; }

    // (Có thể thêm Setter nếu cần...)
    public void setOrderId(String orderId) { this.orderId = orderId; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }
    public void setEmployeeId(String employeeId) { this.employeeId = employeeId; }
    public void setOrderDate(Date orderDate) { this.orderDate = orderDate; }
    public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }
    public void setStatus(String status) { this.status = status; }
    public void setDeleted(boolean deleted) { isDeleted = deleted; }
}