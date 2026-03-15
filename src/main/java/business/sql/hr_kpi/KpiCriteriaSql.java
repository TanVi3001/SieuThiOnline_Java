package business.sql.hr_kpi;

import model.KpiCriteria;
import business.sql.SqlInterface;
import java.util.ArrayList;
import java.util.List;

public class KpiCriteriaSql implements SqlInterface<KpiCriteria> {
    public static KpiCriteriaSql getInstance() {
        return new KpiCriteriaSql();
    }

    @Override public int insert(KpiCriteria t) { return 0; }
    @Override public int update(KpiCriteria t) { return 0; }
    @Override public int delete(String id) { return 0; }
    @Override public ArrayList<KpiCriteria> selectAll() { return new ArrayList<>(); }
    @Override public KpiCriteria selectById(String id) { return null; }

    @Override
    public List<KpiCriteria> selectByCondition(String condition) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}