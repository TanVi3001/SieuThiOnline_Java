package business.sql.rbac;

import business.sql.SqlInterface;
import business.sql.SqlInterface;
import common.db.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import model.account.Token;
import java.util.ArrayList;
import java.util.List;

public class TokenSql implements SqlInterface<Token> {
    public static TokenSql getInstance() {
        return new TokenSql();
    }

    @Override public int insert(Token t) { return 0; }
    @Override public int update(Token t) { return 0; }
    @Override public int delete(String id) { return 0; } // Xóa mềm
    @Override public Token selectById(String id) { return null; }
    
    @Override
    public List<Token> selectByCondition(String condition) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
    @Override
    public ArrayList<Token> selectAll() {
        ArrayList<Token> list = new ArrayList<>();
        // Chỉ lấy những token chưa bị thu hồi
        String sql = "SELECT * FROM TOKENS WHERE is_revoked = 0";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                Token t = new Token();
                t.setTokenId(rs.getString("token_id"));
                t.setAccountId(rs.getString("account_id"));
                t.setTokenValue(rs.getString("token_value"));
                t.setExpiryDate(rs.getTimestamp("expiry_date"));
                // Nếu model Token của bạn có field isRevoked thì set luôn:
                
                list.add(t);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    public void revokeToken(String tokenValue) {
        String sql = "UPDATE TOKENS SET is_revoked = 1 WHERE token_value = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setString(1, tokenValue);
            pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}