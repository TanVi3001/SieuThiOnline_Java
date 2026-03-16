package business.sql.hr_kpi;

import model.employee.Employee;
import business.sql.SqlInterface;
import java.util.ArrayList;
import java.util.List;

public class EmployeeSql implements SqlInterface<Employee> {
    public static EmployeeSql getInstance() {
        return new EmployeeSql();
    }

    @Override public int insert(Employee t) { return 0; }
    @Override public int update(Employee t) { return 0; }
    @Override public int delete(String id) { return 0; }
    @Override public ArrayList<Employee> selectAll() { return new ArrayList<>(); }
    @Override public Employee selectById(String id) { return null; }
    
    // Hàm bổ trợ cập nhật số đơn hàng hoàn thành (theo logic SQL của bạn)
    public int updateCompletedOrders(String employeeId, int count) { return 0; }

    @Override
    public List<Employee> selectByCondition(String condition) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}