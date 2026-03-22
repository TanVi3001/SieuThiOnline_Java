package model.order;

/**
 * Model OrderDetail
 * Leader: Lê Tấn Vĩ (Đã dọn dẹp đống rác của Tùng)
 */
    
public class OrderDetail {

    private final String orderId;
    private final String productId;
    private final int quantity;
    private final double unitPrice;
    private boolean isDeleted;

    public OrderDetail(String orderId, String productId, int quantity, double unitPrice) {
        this.orderId = orderId;
        this.productId = productId;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.isDeleted = false;
    }

    // Sửa lại để trả về giá trị thực tế thay vì ném lỗi
    public String getProductId() {
        return productId;
    }

    public String getOrderId() {
        return orderId;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getUnitPrice() {
        return unitPrice;
    }
    
    public boolean isDeleted() { 
        return isDeleted; 
    }
    
    //Setter
    public void setIsDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }
    
}