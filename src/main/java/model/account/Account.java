package model.account;

import java.sql.Timestamp;

public class Account {
    private String accountId;
    private String userId;
    private String username;
    private String password;
    private String role;      // phân quyền
    private String status;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private int isDeleted;

    // THÊM MỚI: token phiên đăng nhập
    private String token;

    public Account() {
    }

    // Constructor đầy đủ
    public Account(String accountId, String userId, String username, String password, String role,
                   String status, Timestamp createdAt, Timestamp updatedAt, int isDeleted) {
        this.accountId = accountId;
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.role = role;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.isDeleted = isDeleted;
    }

    // Constructor rút gọn
    public Account(String accountId, String username, String password, String role, int isDeleted) {
        this.accountId = accountId;
        this.username = username;
        this.password = password;
        this.role = role;
        this.isDeleted = isDeleted;
    }

    public String getAccountId() { return accountId; }
    public void setAccountId(String accountId) { this.accountId = accountId; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    public Timestamp getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Timestamp updatedAt) { this.updatedAt = updatedAt; }

    public int getIsDeleted() { return isDeleted; }
    public void setIsDeleted(int isDeleted) { this.isDeleted = isDeleted; }

    // TOKEN
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public Object getRoleGroupId() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}