package business.sql.hr_kpi;

import java.util.ArrayList;

public class KpiEvaluationSql {
    public static KpiEvaluationSql getInstance() {
        return new KpiEvaluationSql();
    }

    public int insertEvaluation(Object t) { return 0; }

    // Hàm đặc thù lấy đánh giá theo nhân viên và kỳ đánh giá
    public ArrayList<Object> getEvaluationsByEmployee(String employeeId, String period) {
        return new ArrayList<>();
    }
}