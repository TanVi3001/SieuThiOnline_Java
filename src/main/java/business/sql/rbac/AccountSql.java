package business.sql.rbac;

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

    // 1. Singleton chuẩn (Duy nhất 1 thực thể)
    private static AccountSql instance;

    public AccountSql() {
    }

    public static AccountSql getInstance() {
        if (instance == null) {
            instance = new AccountSql();
        }
        return instance;
    }

    // 2. Hàm quan trọng nhất cho LoginService
    public Account selectByUsername(String username) {
        Account acc = null;
        // SQL: Chỉ lấy những tài khoản chưa bị xóa (is_deleted = 0)
        String sql = "SELECT * FROM ACCOUNTS WHERE username = ? AND is_deleted = 0";

        try (Connection con = DatabaseConnection.getConnection(); PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setString(1, username);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    acc = new Account(
                            rs.getString("ACCOUNT_ID"),
                            rs.getString("USERNAME"),
                            rs.getString("PASSWORD"),
                            rs.getString("ROLE"), // Bây giờ cột này đã tồn tại thật rồi!
                            rs.getInt("IS_DELETED")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi SQL Account: " + e.getMessage());
        }
        return acc;
    }

    @Override
    public List<Account> selectAll() {
        List<Account> list = new ArrayList<>();
        String sql = "SELECT * FROM ACCOUNTS WHERE is_deleted = 0";
        try (Connection con = DatabaseConnection.getConnection(); PreparedStatement pst = con.prepareStatement(sql); ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                list.add(new Account(
                        rs.getString("account_id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("role"),
                        rs.getInt("is_deleted")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public int insert(Account t) {
        return 0;
    } // Sẽ bổ sung khi làm chức năng Tạo TK

    @Override
    public int update(Account t) {
        return 0;
    }

    @Override
    public int delete(String id) {
        return 0;
    }

    @Override
    public Account selectById(String id) {
        return null;
    }

    @Override
    public List<Account> selectByCondition(String condition) {
        return null;
    }

    public String findPassByUsernameAndEmail(String username, String email) {
        String password = null;
        // Query Join giữa ACCOUNTS và USERS để check cả 2 điều kiện
        String sql = "SELECT a.password FROM ACCOUNTS a " +
                     "JOIN USERS u ON a.user_id = u.user_id " +
                     "WHERE a.username = ? AND u.email = ? AND a.is_deleted = 0";

        try (Connection con = common.db.DatabaseConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setString(1, username);
            pst.setString(2, email);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                password = rs.getString("password");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return password;
    }
}