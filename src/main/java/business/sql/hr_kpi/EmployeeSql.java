package business.sql.hr_kpi;

import model.employee.Employee;
import business.sql.SqlInterface;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EmployeeSql implements SqlInterface<Employee> {
    public static EmployeeSql getInstance() {
        return new EmployeeSql();
    }

    @Override public int insert(Employee t) { return 0; }
    @Override public int update(Employee t) { return 0; }
    @Override public int delete(String id) { return 0; }
    @Override public Employee selectById(String id) { return null; }
    
    // Hàm bổ trợ cập nhật số đơn hàng hoàn thành (theo logic SQL của bạn)
    public int updateCompletedOrders(String employeeId, int count) { return 0; }

    @Override
    public List<Employee> selectByCondition(String condition) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
    @Override
    public ArrayList<Employee> selectAll() {
        ArrayList<Employee> list = new ArrayList<>();
        String sql = "SELECT * FROM EMPLOYEES WHERE is_deleted = 0";
        try (Connection con = common.db.DatabaseConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                Employee e = new Employee();
                e.setEmployeeId(rs.getString("employee_id"));
                e.setEmployeeName(rs.getString("employee_name"));
                e.setHireDate(rs.getDate("hire_date"));
                e.setSalaryCoefficient(rs.getBigDecimal("salary_coefficient"));
                e.setTotalCompletedOrders(rs.getInt("total_completed_orders"));
                e.setRoleId(rs.getString("role_id"));
                e.setShiftId(rs.getString("shift_id"));
                e.setIsDeleted(rs.getInt("is_deleted"));
                list.add(e);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }
}