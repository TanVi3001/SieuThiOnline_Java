/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author nguye
 */
import java.math.BigDecimal;

public class Product {

    private String productId;
    private String productName;
    private BigDecimal basePrice;
    private String categoryId;
    private String supplierId;
    private int isDeleted;

    public Product() {
    }

    public Product(String productId, String productName, BigDecimal basePrice,
                   String categoryId, String supplierId, int isDeleted) {
        this.productId = productId;
        this.productName = productName;
        this.basePrice = basePrice;
        this.categoryId = categoryId;
        this.supplierId = supplierId;
        this.isDeleted = isDeleted;
    }

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
}
