package model.order;

import java.sql.Date;

public class Order {
    private String orderId;
    private String customerId;
    private String employeeId;
    private String paymentMethodId;
    private Date orderDate;
    private double totalAmount;
    private String status;
    private String note;
    private boolean isDeleted;

    public Order() {}

    /** Constructor đầy đủ 9 tham số (dùng trong SellPanel / POS) */
    public Order(String orderId, String customerId, String employeeId, String paymentMethodId,
                 Date orderDate, double totalAmount, String status, String note, boolean isDeleted) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.employeeId = employeeId;
        this.paymentMethodId = paymentMethodId;
        this.orderDate = orderDate;
        this.totalAmount = totalAmount;
        this.status = status;
        this.note = note;
        this.isDeleted = isDeleted;
    }

    /** Constructor 7 tham số tương thích ngược (không có paymentMethodId, note) */
    public Order(String orderId, String customerId, String employeeId,
                 Date orderDate, double totalAmount, String status, boolean isDeleted) {
        this(orderId, customerId, employeeId, null, orderDate, totalAmount, status, null, isDeleted);
    }

    /** Constructor 6 tham số tương thích ngược */
    public Order(String id, String cusId, String empId, Date date, double total, String note) {
        this(id, cusId, empId, null, date, total, note, null, false);
    }

    // ── Getters ─────────────────────────────────────────────────────
    public String getOrderId()          { return orderId; }
    public String getCustomerId()       { return customerId; }
    public String getEmployeeId()       { return employeeId; }
    public String getPaymentMethodId()  { return paymentMethodId; }
    public Date getOrderDate()          { return orderDate; }
    public double getTotalAmount()      { return totalAmount; }
    public String getStatus()           { return status; }
    public String getNote()             { return note; }
    public boolean isDeleted()          { return isDeleted; }

    // ── Setters ─────────────────────────────────────────────────────
    public void setOrderId(String orderId)              { this.orderId = orderId; }
    public void setCustomerId(String customerId)        { this.customerId = customerId; }
    public void setEmployeeId(String employeeId)        { this.employeeId = employeeId; }
    public void setPaymentMethodId(String pmId)         { this.paymentMethodId = pmId; }
    public void setOrderDate(Date orderDate)            { this.orderDate = orderDate; }
    public void setTotalAmount(double totalAmount)      { this.totalAmount = totalAmount; }
    public void setStatus(String status)                { this.status = status; }
    public void setNote(String note)                    { this.note = note; }
    public void setDeleted(boolean deleted)             { isDeleted = deleted; }
}