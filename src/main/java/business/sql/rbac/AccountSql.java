package business.sql.rbac;

import business.sql.SqlInterface;
import business.sql.SqlInterface;
import model.account.Account;
import java.util.ArrayList;
import java.util.List;

public class AccountSql implements SqlInterface<Account> {
    public static AccountSql getInstance() {
        return new AccountSql();
    }

    @Override public int insert(Account t) { return 0; }
    @Override public int update(Account t) { return 0; }
    @Override public int delete(String id) { return 0; }
    @Override public ArrayList<Account> selectAll() { return new ArrayList<>(); }
    @Override public Account selectById(String id) { return null; }
    
    // Hàm bổ trợ cho đăng nhập
    public Account login(String username, String password) { return null; }

    @Override
    public List<Account> selectByCondition(String condition) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}