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
        String sql
                = "INSERT INTO " + TBL_ACC
                + " (" + COL_ACC_USER_ID + ", " + COL_ACC_USERNAME + ", " + COL_ACC_PASSWORD + ", "
                + COL_ACC_ROLE + ", " + COL_ACC_STATUS + ", CREATED_AT, " + COL_ACC_IS_DELETED + ") "
                + "VALUES (?, ?, ?, ?, ?, SYSDATE, 0)";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, userId);
            ps.setString(2, username);
            ps.setString(3, bcryptPasswordHash); // MUST be bcrypt (trigger)
            ps.setString(4, role);
            ps.setInt(5, 1);
            return ps.executeUpdate();
        }
    }
}
