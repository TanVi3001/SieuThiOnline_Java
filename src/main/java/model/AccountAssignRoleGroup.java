package model;

import java.sql.Timestamp;

public class AccountAssignRoleGroup {
    private String accountId;
    private String roleGroupId;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private int isDeleted;

    public AccountAssignRoleGroup() {}

    public AccountAssignRoleGroup(String accountId, String roleGroupId, Timestamp createdAt, 
                                  Timestamp updatedAt, int isDeleted) {
        this.accountId = accountId;
        this.roleGroupId = roleGroupId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.isDeleted = isDeleted;
    }

    // Getters and Setters...
}