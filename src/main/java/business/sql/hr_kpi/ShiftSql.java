package business.sql.hr_kpi;

import model.employee.Shift; // Giả định bạn sẽ tạo model trong package tương ứng
import business.sql.SqlInterface;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ShiftSql implements SqlInterface<Shift> {
    public static ShiftSql getInstance() {
        return new ShiftSql();
    }

    @Override public int insert(Shift t) { return 0; }
    @Override public int update(Shift t) { return 0; }
    @Override public int delete(String id) { return 0; }
    @Override public Shift selectById(String id) { return null; }

    @Override
    public List<Shift> selectByCondition(String condition) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
    @Override
    public ArrayList<Shift> selectAll() {
        ArrayList<Shift> list = new ArrayList<>();
        String sql = "SELECT * FROM SHIFTS WHERE is_deleted = 0";
        try (Connection con = common.db.DatabaseConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                Shift s = new Shift();
                s.setShiftId(rs.getString("shift_id"));
                s.setShiftName(rs.getString("shift_name"));
                s.setStartTime(rs.getDate("start_time"));
                s.setEndTime(rs.getDate("end_time"));
                s.setIsDeleted(rs.getInt("is_deleted"));
                list.add(s);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }
}