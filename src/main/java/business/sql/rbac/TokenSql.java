package business.sql.rbac;

import business.sql.SqlInterface;
import business.sql.SqlInterface;
import model.Token;
import java.util.ArrayList;

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
}