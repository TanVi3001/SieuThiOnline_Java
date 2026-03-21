package business.sql.hr_kpi;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import model.account.kpi.KpiCriteria;

public class KpiEvaluationSql {
    public static KpiEvaluationSql getInstance() {
        return new KpiEvaluationSql();
    }

    public int insertEvaluation(Object t) { return 0; }

    // Hàm đặc thù lấy đánh giá theo nhân viên và kỳ đánh giá
    public ArrayList<Object> getEvaluationsByEmployee(String employeeId, String period) {
        return new ArrayList<>();
    }
    
    public ArrayList<KpiCriteria> selectAll() {
        ArrayList<KpiCriteria> list = new ArrayList<>();
        String sql = "SELECT * FROM KPI_CRITERIA WHERE is_deleted = 0";
        try (Connection con = common.db.DatabaseConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                KpiCriteria k = new KpiCriteria();
                k.setKpiId(rs.getString("kpi_id"));
                k.setCriteriaName(rs.getString("criteria_name"));
                k.setCriteriaType(rs.getString("criteria_type"));
                k.setWeight(rs.getBigDecimal("weight"));
                k.setRecordedTime(rs.getTimestamp("recorded_time"));
                k.setMinimumTarget(rs.getBigDecimal("minimum_target"));
                k.setIsDeleted(rs.getInt("is_deleted"));
                list.add(k);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }
}