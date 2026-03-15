package business.sql.rbac;

import business.sql.SqlInterface;
import business.sql.SqlInterface;
import model.Token;
import java.util.ArrayList;
import java.util.List;

public class TokenSql implements SqlInterface<Token> {
    public static TokenSql getInstance() {
        return new TokenSql();
    }

    @Override public int insert(Token t) { return 0; }
    @Override public int update(Token t) { return 0; }
    @Override public int delete(String id) { return 0; } // Xóa mềm
    @Override public ArrayList<Token> selectAll() { return new ArrayList<>(); }
    @Override public Token selectById(String id) { return null; }
    
    public void revokeToken(String tokenValue) { /* Logic hủy token */ }

    @Override
    public List<Token> selectByCondition(String condition) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}