package business.sql.hr_kpi;

import business.sql.SqlInterface;
import common.db.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.employee.Employee;

public class EmployeeSql implements SqlInterface<Employee> {

    public static EmployeeSql getInstance() {
        return new EmployeeSql();
    }

    @Override
    public int insert(Employee t) {
        String sql = "INSERT INTO EMPLOYEES (employee_id, employee_name, phone, email, role_id, gender, is_deleted) "
                + "VALUES (?, ?, ?, ?, ?, ?, 0)";
        try (Connection con = DatabaseConnection.getConnection(); PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setString(1, t.getEmployeeId());
            pst.setString(2, t.getEmployeeName());
            pst.setString(3, t.getPhone());
            pst.setString(4, t.getEmail());
            pst.setString(5, t.getRoleId() != null ? t.getRoleId() : t.getRole());
            pst.setString(6, t.getGender());

            return pst.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Insert failed: " + e.getMessage());
            System.err.println("Code: " + e.getErrorCode());
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int update(Employee t) {
        String sql = "UPDATE EMPLOYEES "
                + "SET employee_name = ?, phone = ?, email = ?, role_id = ?, gender = ? "
                + "WHERE employee_id = ? AND is_deleted = 0";
        try (Connection con = DatabaseConnection.getConnection(); PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setString(1, t.getEmployeeName());
            pst.setString(2, t.getPhone());
            pst.setString(3, t.getEmail());
            pst.setString(4, t.getRoleId() != null ? t.getRoleId() : t.getRole());
            pst.setString(5, t.getGender());
            pst.setString(6, t.getEmployeeId());

            return pst.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Lỗi EmployeeSql.update: " + e.getMessage());
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int delete(String id) {
        String sql = "UPDATE EMPLOYEES SET is_deleted = 1 WHERE employee_id = ?";
        try (Connection con = DatabaseConnection.getConnection(); PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setString(1, id);
            return pst.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Lỗi EmployeeSql.delete: " + e.getMessage());
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public Employee selectById(String id) {
        String sql = "SELECT * FROM EMPLOYEES WHERE employee_id = ? AND is_deleted = 0";
        try (Connection con = DatabaseConnection.getConnection(); PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setString(1, id);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    return map(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi EmployeeSql.selectById: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public int updateCompletedOrders(String employeeId, int count) {
        String sql = "UPDATE EMPLOYEES SET total_completed_orders = ? WHERE employee_id = ? AND is_deleted = 0";
        try (Connection con = DatabaseConnection.getConnection(); PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setInt(1, count);
            pst.setString(2, employeeId);
            return pst.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Lỗi EmployeeSql.updateCompletedOrders: " + e.getMessage());
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public List<Employee> selectByCondition(String condition) {
        ArrayList<Employee> list = new ArrayList<>();
        String sql = "SELECT * FROM EMPLOYEES WHERE is_deleted = 0 " + (condition == null ? "" : condition);

        try (Connection con = DatabaseConnection.getConnection(); PreparedStatement pst = con.prepareStatement(sql); ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                list.add(map(rs));
            }
        } catch (SQLException e) {
            System.err.println("Lỗi EmployeeSql.selectByCondition: " + e.getMessage());
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public ArrayList<Employee> selectAll() {
        ArrayList<Employee> list = new ArrayList<>();
        String sql = "SELECT * FROM EMPLOYEES WHERE is_deleted = 0";
        try (Connection con = DatabaseConnection.getConnection(); PreparedStatement pst = con.prepareStatement(sql); ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                list.add(map(rs));
            }
        } catch (SQLException e) {
            System.err.println("Lỗi EmployeeSql.selectAll: " + e.getMessage());
            e.printStackTrace();
        }
        return list;
    }

    public ArrayList<Employee> searchByName(String name) {
        ArrayList<Employee> list = new ArrayList<>();
        String sql = "SELECT * FROM EMPLOYEES WHERE LOWER(employee_name) LIKE ? AND is_deleted = 0";
        try (Connection con = DatabaseConnection.getConnection(); PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setString(1, "%" + (name == null ? "" : name.trim().toLowerCase()) + "%");
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    list.add(map(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi EmployeeSql.searchByName: " + e.getMessage());
            e.printStackTrace();
        }
        return list;
    }

    public List<Employee> search(String keyword) {
        ArrayList<Employee> list = new ArrayList<>();
        String sql = "SELECT * FROM EMPLOYEES WHERE is_deleted = 0 AND ("
                + "LOWER(employee_id) LIKE ? OR "
                + "LOWER(employee_name) LIKE ? OR "
                + "LOWER(NVL(phone,'')) LIKE ? OR "
                + "LOWER(NVL(email,'')) LIKE ? OR "
                + "LOWER(NVL(role_id,'')) LIKE ? OR "
                + "LOWER(NVL(gender,'')) LIKE ?"
                + ")";

        String k = "%" + (keyword == null ? "" : keyword.trim().toLowerCase()) + "%";

        try (Connection con = DatabaseConnection.getConnection(); PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setString(1, k);
            pst.setString(2, k);
            pst.setString(3, k);
            pst.setString(4, k);
            pst.setString(5, k);
            pst.setString(6, k);

            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    list.add(map(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi EmployeeSql.search: " + e.getMessage());
            e.printStackTrace();
        }
        return list;
    }

    private Employee map(ResultSet rs) throws SQLException {
        Employee e = new Employee();
        e.setEmployeeId(rs.getString("employee_id"));
        e.setEmployeeName(rs.getString("employee_name"));

        try {
            e.setHireDate(rs.getDate("hire_date"));
        } catch (SQLException ignored) {
        }
        try {
            e.setSalaryCoefficient(rs.getBigDecimal("salary_coefficient"));
        } catch (SQLException ignored) {
        }
        try {
            e.setTotalCompletedOrders(rs.getInt("total_completed_orders"));
        } catch (SQLException ignored) {
        }
        try {
            e.setRoleId(rs.getString("role_id"));
        } catch (SQLException ignored) {
        }
        try {
            e.setShiftId(rs.getString("shift_id"));
        } catch (SQLException ignored) {
        }

        e.setIsDeleted(rs.getInt("is_deleted"));

        try {
            e.setPhone(rs.getString("phone"));
        } catch (SQLException ignored) {
        }
        try {
            e.setEmail(rs.getString("email"));
        } catch (SQLException ignored) {
        }
        e.setRole(e.getRoleId()); // hiển thị cột "Chức vụ" bằng role_id
        try {
            e.setGender(rs.getString("gender"));
        } catch (SQLException ignored) {
        }

        return e;
    }
}
