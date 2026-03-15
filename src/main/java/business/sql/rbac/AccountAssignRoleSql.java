package business.sql.rbac;

public class AccountAssignRoleSql {
    public static AccountAssignRoleSql getInstance() { return new AccountAssignRoleSql(); }
    
    public int assignRoleToAccount(String accountId, String roleId) { return 0; }
    public int removeRoleFromAccount(String accountId, String roleId) { return 0; }
}