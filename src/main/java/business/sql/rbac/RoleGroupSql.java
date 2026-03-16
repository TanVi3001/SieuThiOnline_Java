package business.sql.rbac;

import business.sql.SqlInterface;
import model.account.RoleGroup;
import java.util.ArrayList;
import java.util.List;

public class RoleGroupSql implements SqlInterface<RoleGroup> {
    public static RoleGroupSql getInstance() {
        return new RoleGroupSql();
    }

    @Override public int insert(RoleGroup t) { return 0; }
    @Override public int update(RoleGroup t) { return 0; }
    @Override public int delete(String id) { return 0; }
    @Override public ArrayList<RoleGroup> selectAll() { return new ArrayList<>(); }
    @Override public RoleGroup selectById(String id) { return null; }

    @Override
    public List<RoleGroup> selectByCondition(String condition) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}