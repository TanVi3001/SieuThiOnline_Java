package model.product;

import java.math.BigDecimal;

/**
 * @author nguye - Fixed by Leader Vi
 */
public class Product {

    private String productId;
    private String productName;
    private BigDecimal basePrice; // Để BigDecimal cho tiền tệ là chuẩn nhất
    private String categoryId;
    private String supplierId;
    private int isDeleted;
    private int quantity; // THÊM MỚI: Biến lưu số lượng từ bảng Inventory

    public Product() {
    }

    // Constructor đầy đủ để sau này dùng cho nhanh
    public Product(String productId, String productName, BigDecimal basePrice,
                   String categoryId, String supplierId, int isDeleted, int quantity) {
        this.productId = productId;
        this.productName = productName;
        this.basePrice = basePrice;
        this.categoryId = categoryId;
        this.supplierId = supplierId;
        this.isDeleted = isDeleted;
        this.quantity = quantity;
    }

    // --- GETTER & SETTER ---

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public BigDecimal getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(BigDecimal basePrice) {
        this.basePrice = basePrice;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId;
    }

    public int getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(int isDeleted) {
        this.isDeleted = isDeleted;
    }

    // SỬA LẠI HÀM NÀY: Không còn UnsupportedOperationException nữa!
    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}