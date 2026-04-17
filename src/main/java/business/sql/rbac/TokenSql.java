package business.sql.rbac;

import business.sql.SqlInterface;
import common.db.DatabaseConnection;
import model.account.Token;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TokenSql implements SqlInterface<Token> {

    public static TokenSql getInstance() {
        return new TokenSql();
    }

    @Override
    public int insert(Token t) {
        String sql = "INSERT INTO TOKENS (TOKEN_ID, ACCOUNT_ID, TOKEN_VALUE, EXPIRY_DATE, CREATED_AT, IS_REVOKED, IS_DELETED) "
                + "VALUES (?, ?, ?, ?, SYSTIMESTAMP, 0, 0)";
        try (Connection con = DatabaseConnection.getConnection(); PreparedStatement pst = con.prepareStatement(sql)) {

            // Nếu token_id chưa có thì tự tạo
            String tokenId = (t.getTokenId() == null || t.getTokenId().isBlank())
                    ? UUID.randomUUID().toString()
                    : t.getTokenId();

            pst.setString(1, tokenId);
            pst.setString(2, t.getAccountId());
            pst.setString(3, t.getTokenValue());
            pst.setTimestamp(4, t.getExpiryDate()); // nhớ set expiryDate trước khi insert

            return pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int update(Token t) {
        String sql = "UPDATE TOKENS SET ACCOUNT_ID = ?, TOKEN_VALUE = ?, EXPIRY_DATE = ?, IS_REVOKED = ?, IS_DELETED = ? "
                + "WHERE TOKEN_ID = ?";
        try (Connection con = DatabaseConnection.getConnection(); PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setString(1, t.getAccountId());
            pst.setString(2, t.getTokenValue());
            pst.setTimestamp(3, t.getExpiryDate());
            pst.setInt(4, t.getIsRevoked());
            pst.setInt(5, t.getIsDeleted());
            pst.setString(6, t.getTokenId());

            return pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int delete(String id) {
        // Xóa mềm
        String sql = "UPDATE TOKENS SET IS_DELETED = 1 WHERE TOKEN_ID = ?";
        try (Connection con = DatabaseConnection.getConnection(); PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setString(1, id);
            return pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public Token selectById(String id) {
        String sql = "SELECT * FROM TOKENS WHERE TOKEN_ID = ? AND IS_DELETED = 0";
        try (Connection con = DatabaseConnection.getConnection(); PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setString(1, id);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    return mapToken(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ArrayList<Token> selectAll() {
        ArrayList<Token> list = new ArrayList<>();
        String sql = "SELECT * FROM TOKENS WHERE IS_DELETED = 0 ORDER BY CREATED_AT DESC";

        try (Connection con = DatabaseConnection.getConnection(); PreparedStatement pst = con.prepareStatement(sql); ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                list.add(mapToken(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public List<Token> selectByCondition(String condition) {
        ArrayList<Token> list = new ArrayList<>();
        String sql = "SELECT * FROM TOKENS WHERE " + condition; // dùng nội bộ, cẩn thận SQL injection

        try (Connection con = DatabaseConnection.getConnection(); PreparedStatement pst = con.prepareStatement(sql); ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                list.add(mapToken(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // ====== Helper methods ======
    public int revokeToken(String tokenValue) {
        String sql = "UPDATE TOKENS SET IS_REVOKED = 1 WHERE TOKEN_VALUE = ? AND IS_DELETED = 0";
        try (Connection con = DatabaseConnection.getConnection(); PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setString(1, tokenValue);
            return pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public Token findValidToken(String tokenValue) {
        String sql = "SELECT * FROM TOKENS "
                + "WHERE TOKEN_VALUE = ? "
                + "AND IS_REVOKED = 0 "
                + "AND IS_DELETED = 0 "
                + "AND EXPIRY_DATE > SYSTIMESTAMP";

        try (Connection con = DatabaseConnection.getConnection(); PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setString(1, tokenValue);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    return mapToken(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Token mapToken(ResultSet rs) throws SQLException {
        Token t = new Token();
        t.setTokenId(rs.getString("TOKEN_ID"));
        t.setAccountId(rs.getString("ACCOUNT_ID"));
        t.setTokenValue(rs.getString("TOKEN_VALUE"));
        t.setExpiryDate(rs.getTimestamp("EXPIRY_DATE"));

        // Nếu bảng/model có cột này thì map
        try {
            t.setCreatedAt(rs.getTimestamp("CREATED_AT"));
        } catch (Exception ignored) {
        }
        try {
            t.setIsRevoked(rs.getInt("IS_REVOKED"));
        } catch (Exception ignored) {
        }
        try {
            t.setIsDeleted(rs.getInt("IS_DELETED"));
        } catch (Exception ignored) {
        }

        return t;
    }

    public int deleteExpiredTokens() {
        String sql = "DELETE FROM TOKENS "
                + "WHERE EXPIRY_DATE < SYSTIMESTAMP "
                + "   OR IS_REVOKED = 1 "
                + "   OR IS_DELETED = 1";
        try (Connection con = DatabaseConnection.getConnection(); PreparedStatement pst = con.prepareStatement(sql)) {
            return pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }
}
