package business.service;

import business.sql.rbac.AccountActivationSql;
import business.sql.rbac.ActivationTokenSql;
import common.db.DatabaseConnection;
import model.account.ActivationEmployeeInfo;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.SQLException;

public class AccountActivationService {

    private final AccountActivationSql accountSql = new AccountActivationSql();
    private final ActivationTokenSql tokenSql = new ActivationTokenSql();

    private Connection getConnection() throws SQLException {
        Connection con = DatabaseConnection.getConnection();
        if (con == null) throw new SQLException("Không thể kết nối DB.");
        return con;
    }

    /**
     * Stage 1: Nhân viên nhập CODE (token) từ email.
     * - token hợp lệ + chưa used + chưa hết hạn
     * - employee tồn tại + chưa deleted
     * - chưa có account
     */
    public ActivationEmployeeInfo checkAndFetchActivation(String code) throws SQLException {
        if (code == null || code.isBlank()) return null;

        try (Connection con = getConnection()) {
            String empId = tokenSql.getEmployeeIdIfValid(con, code.trim());
            if (empId == null) return null;

            ActivationEmployeeInfo info = accountSql.getEmployeeInfo(con, empId);
            if (info == null) return null;

            if (accountSql.existsAccountByUserId(con, empId)) return null;

            return info;
        }
    }

    /**
     * Stage 2: Nhân viên dùng CODE để kích hoạt (không dùng empId trực tiếp).
     * Backend sẽ BCrypt password rồi insert ACCOUNTS, sau đó mark token USED_AT.
     */
    public void activateAccountByCode(String code, String username, String passwordPlain) throws SQLException {
        if (code == null || code.isBlank()) throw new IllegalArgumentException("Mã kích hoạt không được rỗng.");
        if (username == null || username.isBlank()) throw new IllegalArgumentException("Username không được rỗng.");
        if (passwordPlain == null || passwordPlain.isBlank()) throw new IllegalArgumentException("Password không được rỗng.");

        final String defaultRole = "R_STAFF_SALE"; // TODO: chỉnh theo convention role của bạn

        Connection con = null;
        boolean oldAutoCommit = true;

        try {
            con = getConnection();
            oldAutoCommit = con.getAutoCommit();
            con.setAutoCommit(false);

            String empId = tokenSql.getEmployeeIdIfValid(con, code.trim());
            if (empId == null) {
                throw new IllegalStateException("Mã kích hoạt không hợp lệ / đã dùng / đã hết hạn.");
            }

            ActivationEmployeeInfo info = accountSql.getEmployeeInfo(con, empId);
            if (info == null) {
                throw new IllegalStateException("Nhân viên không tồn tại hoặc đã bị xóa.");
            }

            if (accountSql.existsAccountByUserId(con, empId)) {
                throw new IllegalStateException("Tài khoản đã được kích hoạt trước đó.");
            }

            if (accountSql.existsAccountByUsername(con, username.trim())) {
                throw new IllegalStateException("Username đã tồn tại.");
            }

            // BCrypt hash để qua trigger
            String bcryptHash = BCrypt.hashpw(passwordPlain, BCrypt.gensalt(10));

            int inserted = accountSql.insertAccount(con, empId, username.trim(), bcryptHash, defaultRole);
            if (inserted != 1) {
                throw new IllegalStateException("Tạo tài khoản thất bại.");
            }

            tokenSql.markUsed(con, code.trim());

            con.commit();
        } catch (SQLException | RuntimeException ex) {
            if (con != null) {
                try { con.rollback(); } catch (SQLException ignore) {}
            }
            throw ex;
        } finally {
            if (con != null) {
                try { con.setAutoCommit(oldAutoCommit); } catch (SQLException ignore) {}
                DatabaseConnection.closeConnection(con);
            }
        }
    }
}