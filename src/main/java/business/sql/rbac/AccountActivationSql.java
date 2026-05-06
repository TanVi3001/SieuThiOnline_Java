package business.sql.rbac;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import model.account.ActivationEmployeeInfo;

public class AccountActivationSql {

    // EMPLOYEES
    private static final String TBL_EMP = "EMPLOYEES";
    private static final String COL_EMP_ID = "EMPLOYEE_ID";
    private static final String COL_FULLNAME = "EMPLOYEE_NAME";
    private static final String COL_PHONE = "PHONE";
    private static final String COL_EMAIL = "EMAIL";
    private static final String COL_EMP_IS_DELETED = "IS_DELETED";

    // ACCOUNTS
    private static final String TBL_ACC = "ACCOUNTS";
    private static final String COL_ACC_USER_ID = "USER_ID";
    private static final String COL_ACC_USERNAME = "USERNAME";
    private static final String COL_ACC_PASSWORD = "PASSWORD";
    private static final String COL_ACC_ROLE = "ROLE";
    private static final String COL_ACC_STATUS = "STATUS";
    private static final String COL_ACC_IS_DELETED = "IS_DELETED";

    public ActivationEmployeeInfo getEmployeeInfo(Connection con, String empId) throws SQLException {
        String sqlEmp
                = "SELECT " + COL_EMP_ID + ", " + COL_FULLNAME + ", " + COL_PHONE + ", " + COL_EMAIL + " "
                + "FROM " + TBL_EMP + " "
                + "WHERE " + COL_EMP_ID + " = ? AND NVL(" + COL_EMP_IS_DELETED + ", 0) = 0";

        try (PreparedStatement ps = con.prepareStatement(sqlEmp)) {
            ps.setString(1, empId);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    return null;
                }
                return new ActivationEmployeeInfo(
                        rs.getString(COL_EMP_ID),
                        rs.getString(COL_FULLNAME),
                        rs.getString(COL_PHONE),
                        rs.getString(COL_EMAIL)
                );
            }
        }
    }

    public boolean existsAccountByUserId(Connection con, String userId) throws SQLException {
        String sql
                = "SELECT 1 FROM " + TBL_ACC
                + " WHERE " + COL_ACC_USER_ID + " = ? AND NVL(" + COL_ACC_IS_DELETED + ", 0) = 0";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    public boolean existsAccountByUsername(Connection con, String username) throws SQLException {
        String sql
                = "SELECT 1 FROM " + TBL_ACC
                + " WHERE " + COL_ACC_USERNAME + " = ? AND NVL(" + COL_ACC_IS_DELETED + ", 0) = 0";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    public int insertAccount(Connection con, String userId, String username, String bcryptPasswordHash, String role)
            throws SQLException {

        String accountId = "ACC" + System.currentTimeMillis();

        String sql
                = "INSERT INTO ACCOUNTS (ACCOUNT_ID, USER_ID, USERNAME, PASSWORD, STATUS, CREATED_AT, IS_DELETED) "
                + "VALUES (?, ?, ?, ?, ?, SYSDATE, 0)";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, accountId);
            ps.setString(2, userId);
            ps.setString(3, username);
            ps.setString(4, bcryptPasswordHash);
            ps.setString(5, "Hoạt động"); // hoặc 1 nếu DB dùng number, tùy schema bạn
            return ps.executeUpdate();
        }
    }

    public boolean existsUserById(Connection con, String userId) throws SQLException {
        String sql = "SELECT 1 FROM USERS WHERE user_id = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    public int insertUser(Connection con, String userId, String fullName, String email, String phone) throws SQLException {
        String sql = "INSERT INTO USERS (user_id, full_name, email, phone_number) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, userId);
            ps.setString(2, fullName);
            ps.setString(3, email);
            ps.setString(4, phone);
            return ps.executeUpdate();
        }
    }
}
