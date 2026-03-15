package business.sql.hr_kpi;

import model.Shift; // Giả định bạn sẽ tạo model trong package tương ứng
import business.sql.SqlInterface;
import java.util.ArrayList;

public class ShiftSql implements SqlInterface<Shift> {
    public static ShiftSql getInstance() {
        return new ShiftSql();
    }

    @Override public int insert(Shift t) { return 0; }
    @Override public int update(Shift t) { return 0; }
    @Override public int delete(String id) { return 0; }
    @Override public ArrayList<Shift> selectAll() { return new ArrayList<>(); }
    @Override public Shift selectById(String id) { return null; }
}