package business.sql.rbac;

import business.sql.SqlInterface;
import business.sql.SqlInterface;
import model.Role;
import java.util.ArrayList;

public class RoleSql implements SqlInterface<Role> {
    public static RoleSql getInstance() {
        return new RoleSql();
    }

    @Override public int insert(Role t) { return 0; }
    @Override public int update(Role t) { return 0; }
    @Override public int delete(String id) { return 0; }
    @Override public ArrayList<Role> selectAll() { return new ArrayList<>(); }
    @Override public Role selectById(String id) { return null; }
}