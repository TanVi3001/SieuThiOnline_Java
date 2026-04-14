package business.sql.rbac;

import business.sql.SqlInterface;
import common.db.DatabaseConnection;
import common.security.PasswordUtil;
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

    /**
     * Singleton instance.
     *
     * @return AccountSql instance
     */
    public static AccountSql getInstance() {
        if (instance == null) {
            instance = new AccountSql();
        }
        return instance;
    }

    /**
     * Lấy account theo username (phục vụ login).
     *
     * @param username tên đăng nhập
     * @return Account nếu tồn tại, ngược lại null
     */
    public Account selectByUsername(String username) {
        Account acc = null;
        String sql = "SELECT account_id, username, password, is_deleted "
                + "FROM ACCOUNTS WHERE username = ? AND is_deleted = 0";

        try (Connection con = DatabaseConnection.getConnection(); PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setString(1, username);

            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    acc = new Account(
                            rs.getString("account_id"),
                            rs.getString("username"),
                            rs.getString("password"),
                            null, // role chưa có trực tiếp trong ACCOUNTS
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

    /**
     * Lấy tất cả account chưa xóa mềm.
     *
     * @return danh sách account
     */
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
            System.err.println("Lỗi SQL Account.selectAll: " + e.getMessage());
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Chưa dùng.
     *
     * @param t account
     * @return số dòng ảnh hưởng
     */
    @Override
    public int insert(Account t) {
        return 0;
    }

    /**
     * Chưa dùng.
     *
     * @param t account
     * @return số dòng ảnh hưởng
     */
    @Override
    public int update(Account t) {
        return 0;
    }

    /**
     * Xóa mềm account theo id.
     *
     * @param id account_id
     * @return số dòng ảnh hưởng
     */
    @Override
    public int delete(String id) {
        String sql = "UPDATE ACCOUNTS SET is_deleted = 1 WHERE account_id = ?";
        try (Connection con = DatabaseConnection.getConnection(); PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setString(1, id);
            return pst.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Lỗi SQL Account.delete: " + e.getMessage());
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * Lấy account theo account_id.
     *
     * @param id account_id
     * @return Account hoặc null
     */
    @Override
    public Account selectById(String id) {
        String sql = "SELECT account_id, username, password, is_deleted "
                + "FROM ACCOUNTS WHERE account_id = ? AND is_deleted = 0";
        try (Connection con = DatabaseConnection.getConnection(); PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setString(1, id);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    return new Account(
                            rs.getString("account_id"),
                            rs.getString("username"),
                            rs.getString("password"),
                            null,
                            rs.getInt("is_deleted")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi SQL Account.selectById: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Chưa dùng.
     *
     * @param condition điều kiện
     * @return danh sách account
     */
    @Override
    public List<Account> selectByCondition(String condition) {
        return new ArrayList<>();
    }

    /**
     * Tìm password hash theo username + email. (Không trả plain password).
     *
     * @param username tên đăng nhập
     * @param email email user
     * @return password hash hoặc null
     */
    public String findPassByUsernameAndEmail(String username, String email) {
        String passwordHash = null;
        String sql = "SELECT a.password FROM ACCOUNTS a "
                + "JOIN USERS u ON a.user_id = u.user_id "
                + "WHERE a.username = ? AND u.email = ? AND a.is_deleted = 0";

        try (Connection con = DatabaseConnection.getConnection(); PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setString(1, username);
            pst.setString(2, email);

            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    passwordHash = rs.getString("password");
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi SQL Account.findPassByUsernameAndEmail: " + e.getMessage());
            e.printStackTrace();
        }
        return passwordHash;
    }

    /**
     * Đăng ký tài khoản mới: - insert USERS - insert ACCOUNTS (password đã băm
     * BCrypt)
     *
     * @param fullName họ tên
     * @param email email
     * @param phone số điện thoại
     * @param username tên đăng nhập
     * @param rawPassword mật khẩu thô
     * @return true nếu thành công, false nếu thất bại
     */
    public boolean register(String fullName, String email, String phone, String username, String rawPassword) {
        String userId = "USR" + (System.currentTimeMillis() % 1000000);
        String accId = "ACC" + (System.currentTimeMillis() % 1000000);

        String sqlCheckUser = "SELECT 1 FROM ACCOUNTS WHERE username = ? AND is_deleted = 0";
        String sqlCheckEmail = "SELECT 1 FROM USERS WHERE email = ? AND is_deleted = 0";
        String sqlUser = "INSERT INTO USERS (user_id, full_name, email, phone_number) VALUES (?, ?, ?, ?)";
        String sqlAccount = "INSERT INTO ACCOUNTS (account_id, user_id, username, password, status) "
                + "VALUES (?, ?, ?, ?, 'Hoạt động')";

        Connection con = null;
        try {
            con = DatabaseConnection.getConnection();
            con.setAutoCommit(false);

            // Check trùng username
            try (PreparedStatement pstCheckUser = con.prepareStatement(sqlCheckUser)) {
                pstCheckUser.setString(1, username);
                try (ResultSet rs = pstCheckUser.executeQuery()) {
                    if (rs.next()) {
                        con.rollback();
                        return false;
                    }
                }
            }

            // Check trùng email
            try (PreparedStatement pstCheckEmail = con.prepareStatement(sqlCheckEmail)) {
                pstCheckEmail.setString(1, email);
                try (ResultSet rs = pstCheckEmail.executeQuery()) {
                    if (rs.next()) {
                        con.rollback();
                        return false;
                    }
                }
            }

            String passwordHash = PasswordUtil.hash(rawPassword);

            try (PreparedStatement pstUser = con.prepareStatement(sqlUser); PreparedStatement pstAcc = con.prepareStatement(sqlAccount)) {

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
            }

            con.commit();
            return true;

        } catch (Exception e) {
            if (con != null) {
                try {
                    con.rollback();
                } catch (Exception ignored) {
                }
            }
            System.err.println("Lỗi Account.register: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            if (con != null) {
                try {
                    con.setAutoCommit(true);
                    con.close();
                } catch (Exception ignored) {
                }
            }
        }
    }

    /**
     * Cập nhật password hash theo account_id.
     *
     * @param accountId account_id
     * @param passwordHash mật khẩu đã hash
     * @return true nếu cập nhật thành công
     */
    public boolean updatePasswordByAccountId(String accountId, String passwordHash) {
        String sql = "UPDATE ACCOUNTS SET password = ?, updated_at = CURRENT_TIMESTAMP WHERE account_id = ?";

        try (Connection con = DatabaseConnection.getConnection(); PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setString(1, passwordHash);
            pst.setString(2, accountId);

            return pst.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi SQL Account.updatePasswordByAccountId: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Migrate password plain text -> BCrypt hash (chạy 1 lần).
     *
     * @return số account đã được migrate
     */
    public int migratePlainPasswordsToBCrypt() {
        int migrated = 0;

        String sqlSelect = "SELECT account_id, password FROM ACCOUNTS WHERE is_deleted = 0";
        String sqlUpdate = "UPDATE ACCOUNTS SET password = ?, updated_at = CURRENT_TIMESTAMP WHERE account_id = ?";

        try (Connection con = DatabaseConnection.getConnection(); PreparedStatement pstSelect = con.prepareStatement(sqlSelect); ResultSet rs = pstSelect.executeQuery(); PreparedStatement pstUpdate = con.prepareStatement(sqlUpdate)) {

            while (rs.next()) {
                String accountId = rs.getString("account_id");
                String pwd = rs.getString("password");

                if (pwd != null && !PasswordUtil.isBCryptHash(pwd)) {
                    String hash = PasswordUtil.hash(pwd);
                    pstUpdate.setString(1, hash);
                    pstUpdate.setString(2, accountId);
                    pstUpdate.addBatch();
                    migrated++;
                }
            }

            pstUpdate.executeBatch();

        } catch (SQLException e) {
            System.err.println("Lỗi SQL Account.migratePlainPasswordsToBCrypt: " + e.getMessage());
            e.printStackTrace();
        }

        return migrated;
    }
    
    /**
     * Tìm Username dựa trên Email (phục vụ luồng Quên mật khẩu an toàn).
     */
    public String findUsernameByEmail(String email) {
        String sql = "SELECT a.username FROM ACCOUNTS a "
                   + "JOIN USERS u ON a.user_id = u.user_id "
                   + "WHERE u.email = ? AND a.is_deleted = 0";
        try (Connection con = DatabaseConnection.getConnection(); 
             PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setString(1, email);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) return rs.getString("username");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Lưu mã OTP vào bảng OTP_STORAGE. 
     * Nếu email đã tồn tại thì cập nhật mã mới và gia hạn thêm 5 phút.
     */
    public boolean saveOTP(String email, String otp) {
        // Oracle: sysdate + 5/1440 tương đương với thời gian hiện tại cộng 5 phút
        String sql = "MERGE INTO OTP_STORAGE t "
                   + "USING (SELECT ? as email, ? as otp FROM dual) s "
                   + "ON (t.email = s.email) "
                   + "WHEN MATCHED THEN UPDATE SET t.otp_code = s.otp, t.expiry_time = sysdate + 5/1440 "
                   + "WHEN NOT MATCHED THEN INSERT (email, otp_code, expiry_time) VALUES (s.email, s.otp, sysdate + 5/1440)";
        try (Connection con = DatabaseConnection.getConnection(); 
             PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setString(1, email);
            pst.setString(2, otp);
            return pst.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Kiểm tra mã OTP có khớp và còn hạn (trong vòng 5 phút) hay không.
     */
    public boolean validateOTP(String email, String otp) {
        String sql = "SELECT 1 FROM OTP_STORAGE WHERE email = ? AND otp_code = ? AND expiry_time > sysdate";
        try (Connection con = DatabaseConnection.getConnection(); 
             PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setString(1, email);
            pst.setString(2, otp);
            try (ResultSet rs = pst.executeQuery()) {
                return rs.next(); // Nếu có dòng trả về nghĩa là OTP hợp lệ
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Cập nhật mật khẩu mới thông qua Email (Dùng sau khi đã xác thực OTP thành công).
     */
    public boolean updatePasswordByEmail(String email, String rawPassword) {
        String passwordHash = PasswordUtil.hash(rawPassword);
        String sql = "UPDATE ACCOUNTS SET password = ?, updated_at = CURRENT_TIMESTAMP "
                   + "WHERE user_id = (SELECT user_id FROM USERS WHERE email = ?)";
        try (Connection con = DatabaseConnection.getConnection(); 
             PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setString(1, passwordHash);
            pst.setString(2, email);
            return pst.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
