package model;

import java.sql.Timestamp;

public class AccountAssignRole {
    private String accountId;
    private String roleId;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private int isDeleted;

    public AccountAssignRole() {}

    public AccountAssignRole(String accountId, String roleId, Timestamp createdAt, 
                             Timestamp updatedAt, int isDeleted) {
        this.accountId = accountId;
        this.roleId = roleId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.isDeleted = isDeleted;
    }

    // Getters and Setters...
}