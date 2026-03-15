package business.sql.rbac;

import business.sql.SqlInterface;
import business.sql.SqlInterface;
import model.Function;
import java.util.ArrayList;

public class FunctionSql implements SqlInterface<Function> {
    public static FunctionSql getInstance() {
        return new FunctionSql();
    }

    @Override public int insert(Function t) { return 0; }
    @Override public int update(Function t) { return 0; }
    @Override public int delete(String id) { return 0; }
    @Override public ArrayList<Function> selectAll() { return new ArrayList<>(); }
    @Override public Function selectById(String id) { return null; }
}