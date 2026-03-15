package business.sql.rbac;

import business.sql.SqlInterface;
import business.sql.SqlInterface;
import model.User; // Đảm bảo bạn có class User trong package model
import java.util.ArrayList;

public class UserSql implements SqlInterface<User> {
    public static UserSql getInstance() {
        return new UserSql();
    }

    @Override public int insert(User t) { return 0; }
    @Override public int update(User t) { return 0; }
    @Override public int delete(String id) { return 0; }
    @Override public ArrayList<User> selectAll() { return new ArrayList<>(); }
    @Override public User selectById(String id) { return null; }
}