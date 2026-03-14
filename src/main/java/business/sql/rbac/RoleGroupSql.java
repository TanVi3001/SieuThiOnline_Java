package business.sql.rbac;

import business.sql.SqlInterface;
import business.sql.SqlInterface;
import model.RoleGroup;
import java.util.ArrayList;

public class RoleGroupSql implements SqlInterface<RoleGroup> {
    public static RoleGroupSql getInstance() {
        return new RoleGroupSql();
    }

    @Override public int insert(RoleGroup t) { return 0; }
    @Override public int update(RoleGroup t) { return 0; }
    @Override public int delete(String id) { return 0; }
    @Override public ArrayList<RoleGroup> selectAll() { return new ArrayList<>(); }
    @Override public RoleGroup selectById(String id) { return null; }
}