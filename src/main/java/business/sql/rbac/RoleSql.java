package business.sql.rbac;

import business.sql.SqlInterface;
import model.account.Role;
import java.util.ArrayList;
import java.util.List;

public class RoleSql implements SqlInterface<Role> {
    public static RoleSql getInstance() {
        return new RoleSql();
    }

    @Override public int insert(Role t) { return 0; }
    @Override public int update(Role t) { return 0; }
    @Override public int delete(String id) { return 0; }
    @Override public ArrayList<Role> selectAll() { return new ArrayList<>(); }
    @Override public Role selectById(String id) { return null; }

    @Override
    public List<Role> selectByCondition(String condition) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}