package business.sql.rbac;

import common.utils.PasswordUtils;
import business.sql.SqlInterface;
import common.db.DatabaseConnection;
import model.account.Account;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AccountSql implements SqlInterface<Account> {

    private static AccountSql instance;

    public AccountSql() {
    }

    public static AccountSql getInstance() {
        if (instance == null) {
            instance = new AccountSql();
        }
        return instance;
    }

    /**
     * SỬA ĐỔI 1: Lấy thêm RoleId khi đăng nhập để điều hướng Dashboard
     */
    public Account selectByUsername(String username) {
        Account acc = null;
        String sql = "SELECT a.account_id, a.username, a.password, a.is_deleted, aar.role_id "
                + "FROM ACCOUNTS a "
                + "LEFT JOIN ACCOUNT_ASSIGN_ROLE aar ON a.account_id = aar.account_id "
                + "WHERE a.username = ? AND a.is_deleted = 0";

        try (Connection con = DatabaseConnection.getConnection(); PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setString(1, username);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    acc = new Account(
                            rs.getString("account_id"),
                            rs.getString("username"),
                            rs.getString("password"),
                            rs.getString("role_id"), // Lấy mã role phục vụ phân loại Dashboard
                            rs.getInt("is_deleted")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi SQL Account.selectByUsername: " + e.getMessage());
            e.printStackTrace();
        }
        return acc;
    }

    @Override
    public List<Account> selectAll() {
        List<Account> list = new ArrayList<>();
        String sql = "SELECT account_id, username, password, is_deleted FROM ACCOUNTS WHERE is_deleted = 0";
        try (Connection con = DatabaseConnection.getConnection(); PreparedStatement pst = con.prepareStatement(sql); ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                list.add(new Account(
                        rs.getString("account_id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        null,
                        rs.getInt("is_deleted")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override public int insert(Account t) { return 0; }
    @Override public int update(Account t) { return 0; }

    @Override
    public int delete(String id) {
        String sql = "UPDATE ACCOUNTS SET is_deleted = 1 WHERE account_id = ?";
        try (Connection con = DatabaseConnection.getConnection(); PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setString(1, id);
            return pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public Account selectById(String id) {
        String sql = "SELECT account_id, username, password, is_deleted FROM ACCOUNTS WHERE account_id = ? AND is_deleted = 0";
        try (Connection con = DatabaseConnection.getConnection(); PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setString(1, id);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    return new Account(rs.getString("account_id"), rs.getString("username"), rs.getString("password"), null, rs.getInt("is_deleted"));
                }
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    @Override public List<Account> selectByCondition(String condition) { return new ArrayList<>(); }

    public String findPassByUsernameAndEmail(String username, String email) {
        String passwordHash = null;
        String sql = "SELECT a.password FROM ACCOUNTS a JOIN USERS u ON a.user_id = u.user_id WHERE a.username = ? AND u.email = ? AND a.is_deleted = 0";
        try (Connection con = DatabaseConnection.getConnection(); PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setString(1, username);
            pst.setString(2, email);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) passwordHash = rs.getString("password");
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return passwordHash;
    }

    /**
     * SỬA ĐỔI 2: Tự động phân loại quyền Admin/Store Manager dựa trên username
     */
    public boolean register(String fullName, String email, String phone, String username, String rawPassword) {
        String userId = "USR" + (System.currentTimeMillis() % 1000000);
        String accId = "ACC" + (System.currentTimeMillis() % 1000000);

        String sqlCheckUser = "SELECT 1 FROM ACCOUNTS WHERE username = ? AND is_deleted = 0";
        String sqlCheckEmail = "SELECT 1 FROM USERS WHERE email = ? AND is_deleted = 0";
        String sqlUser = "INSERT INTO USERS (user_id, full_name, email, phone_number) VALUES (?, ?, ?, ?)";
        String sqlAccount = "INSERT INTO ACCOUNTS (account_id, user_id, username, password, status) VALUES (?, ?, ?, ?, 'Hoạt động')";

        Connection con = null;
        try {
            con = DatabaseConnection.getConnection();
            con.setAutoCommit(false);

            // Check trùng username
            try (PreparedStatement pstCheckUser = con.prepareStatement(sqlCheckUser)) {
                pstCheckUser.setString(1, username);
                try (ResultSet rs = pstCheckUser.executeQuery()) {
                    if (rs.next()) { con.rollback(); return false; }
                }
            }

            // Check trùng email
            try (PreparedStatement pstCheckEmail = con.prepareStatement(sqlCheckEmail)) {
                pstCheckEmail.setString(1, email);
                try (ResultSet rs = pstCheckEmail.executeQuery()) {
                    if (rs.next()) { con.rollback(); return false; }
                }
            }
            
            String passwordHash = PasswordUtils.hash(rawPassword);

            // Thực thi lưu thông tin chính
            try (PreparedStatement pstUser = con.prepareStatement(sqlUser); 
                 PreparedStatement pstAcc = con.prepareStatement(sqlAccount)) {

                pstUser.setString(1, userId);
                pstUser.setString(2, fullName);
                pstUser.setString(3, email);
                pstUser.setString(4, phone);
                pstUser.executeUpdate();

                pstAcc.setString(1, accId);
                pstAcc.setString(2, userId);
                pstAcc.setString(3, username);
                pstAcc.setString(4, passwordHash);
                pstAcc.executeUpdate();

                // --- LOGIC PHÂN QUYỀN TỰ ĐỘNG DÁN VÀO ĐÂY ---
                String roleId = "R_STAFF_SALE"; 
                String lowerUser = username.toLowerCase();
                
                if (lowerUser.contains("admin")) {
                    roleId = "R_ADMIN_ALL"; 
                } else if (lowerUser.contains("store_manager")) {
                    roleId = "R_STORE_MNG"; 
                }

                String sqlAssignRole = "INSERT INTO ACCOUNT_ASSIGN_ROLE (account_id, role_id) VALUES (?, ?)";
                try (PreparedStatement pstRole = con.prepareStatement(sqlAssignRole)) {
                    pstRole.setString(1, accId);
                    pstRole.setString(2, roleId);
                    pstRole.executeUpdate();
                }
            }

            con.commit();
            return true;

        } catch (Exception e) {
            if (con != null) { try { con.rollback(); } catch (Exception ignored) {} }
            e.printStackTrace();
            return false;
        } finally {
            if (con != null) { try { con.setAutoCommit(true); con.close(); } catch (Exception ignored) {} }
        }
    }

    public boolean updatePasswordByAccountId(String accountId, String passwordHash) {
        String sql = "UPDATE ACCOUNTS SET password = ?, updated_at = CURRENT_TIMESTAMP WHERE account_id = ?";
        try (Connection con = DatabaseConnection.getConnection(); PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setString(1, passwordHash);
            pst.setString(2, accountId);
            return pst.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    /**
     * GIỮ LẠI THEO YÊU CẦU: Hàm Migrate BCrypt
     */
    public int migratePlainPasswordsToBCrypt() {
        int migrated = 0;
        String sqlSelect = "SELECT account_id, password FROM ACCOUNTS WHERE is_deleted = 0";
        String sqlUpdate = "UPDATE ACCOUNTS SET password = ?, updated_at = CURRENT_TIMESTAMP WHERE account_id = ?";
        try (Connection con = DatabaseConnection.getConnection(); 
             PreparedStatement pstSelect = con.prepareStatement(sqlSelect); 
             ResultSet rs = pstSelect.executeQuery(); 
             PreparedStatement pstUpdate = con.prepareStatement(sqlUpdate)) {
            while (rs.next()) {
                String accountId = rs.getString("account_id");
                String pwd = rs.getString("password");
                if (pwd != null && !PasswordUtils.isBCryptHash(pwd)) {
                    String hash = PasswordUtils.hash(pwd);
                    pstUpdate.setString(1, hash);
                    pstUpdate.setString(2, accountId);
                    pstUpdate.addBatch();
                    migrated++;
                }
            }
            pstUpdate.executeBatch();
        } catch (SQLException e) { e.printStackTrace(); }
        return migrated;
    }
    
    public String findUsernameByEmail(String email) {
        String sql = "SELECT a.username FROM ACCOUNTS a JOIN USERS u ON a.user_id = u.user_id WHERE u.email = ? AND a.is_deleted = 0";
        try (Connection con = DatabaseConnection.getConnection(); PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setString(1, email);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) return rs.getString("username");
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    public boolean saveOTP(String email, String otp) {
        String sql = "MERGE INTO OTP_STORAGE t USING (SELECT ? as email, ? as otp FROM dual) s ON (t.email = s.email) "
                   + "WHEN MATCHED THEN UPDATE SET t.otp_code = s.otp, t.expiry_time = sysdate + 5/1440 "
                   + "WHEN NOT MATCHED THEN INSERT (email, otp_code, expiry_time) VALUES (s.email, s.otp, sysdate + 5/1440)";
        try (Connection con = DatabaseConnection.getConnection(); PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setString(1, email); pst.setString(2, otp);
            return pst.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    public boolean validateOTP(String email, String otp) {
        String sql = "SELECT 1 FROM OTP_STORAGE WHERE email = ? AND otp_code = ? AND expiry_time > sysdate";
        try (Connection con = DatabaseConnection.getConnection(); PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setString(1, email); pst.setString(2, otp);
            try (ResultSet rs = pst.executeQuery()) { return rs.next(); }
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    public boolean updatePasswordByEmail(String email, String rawPassword) {
        String passwordHash = PasswordUtils.hash(rawPassword);
        String sql = "UPDATE ACCOUNTS SET password = ?, updated_at = CURRENT_TIMESTAMP WHERE user_id = (SELECT user_id FROM USERS WHERE email = ?)";
        try (Connection con = DatabaseConnection.getConnection(); PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setString(1, passwordHash); pst.setString(2, email);
            return pst.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }
    
    public boolean checkDuplicateUsername(String username) {
        String sql = "SELECT 1 FROM ACCOUNTS WHERE username = ? AND is_deleted = 0";
        try (Connection con = DatabaseConnection.getConnection(); PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setString(1, username);
            try (ResultSet rs = pst.executeQuery()) { return rs.next(); }
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    public boolean checkDuplicateEmail(String email) {
        String sql = "SELECT 1 FROM USERS WHERE email = ? AND is_deleted = 0";
        try (Connection con = DatabaseConnection.getConnection(); PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setString(1, email);
            try (ResultSet rs = pst.executeQuery()) { return rs.next(); }
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    public boolean checkDuplicatePhone(String phone) {
        String sql = "SELECT 1 FROM USERS WHERE phone_number = ? AND is_deleted = 0";
        try (Connection con = DatabaseConnection.getConnection(); PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setString(1, phone);
            try (ResultSet rs = pst.executeQuery()) { return rs.next(); }
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }
}