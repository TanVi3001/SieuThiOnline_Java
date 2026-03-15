package business.sql.hr_kpi;

import model.KpiCriteria;
import business.sql.SqlInterface;
import java.util.ArrayList;

public class KpiCriteriaSql implements SqlInterface<KpiCriteria> {
    public static KpiCriteriaSql getInstance() {
        return new KpiCriteriaSql();
    }

    @Override public int insert(KpiCriteria t) { return 0; }
    @Override public int update(KpiCriteria t) { return 0; }
    @Override public int delete(String id) { return 0; }
    @Override public ArrayList<KpiCriteria> selectAll() { return new ArrayList<>(); }
    @Override public KpiCriteria selectById(String id) { return null; }
}