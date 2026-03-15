package model;

import java.sql.Timestamp;

public class RoleGroupAssignRole {
    private String roleGroupId;
    private String roleId;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private int isDeleted;

    public RoleGroupAssignRole() {}

    public RoleGroupAssignRole(String roleGroupId, String roleId, Timestamp createdAt, 
                               Timestamp updatedAt, int isDeleted) {
        this.roleGroupId = roleGroupId;
        this.roleId = roleId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.isDeleted = isDeleted;
    }

    // Getters and Setters...
}