package business.sql.sales_order;

import model.order.Customer;
import business.sql.SqlInterface;
import common.db.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CustomersSql implements SqlInterface<Customer> {
    public static CustomersSql getInstance() { return new CustomersSql(); }

    @Override public int insert(Customer t) { return 0; }
    @Override public int update(Customer t) { return 0; }
    @Override public int delete(String id) { return 0; }
    @Override public Customer selectById(String id) { return null; }
    
    // Hàm đặc thù: Cập nhật điểm thưởng
    public int updateRewardPoints(String customerId, int points) { return 0; }

    @Override
    public List<Customer> selectByCondition(String condition) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    @Override
    public ArrayList<Customer> selectAll() {
        ArrayList<Customer> list = new ArrayList<>();
        String sql = "SELECT * FROM CUSTOMERS WHERE is_deleted = 0";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                Customer c = new Customer();
                c.setCustomerId(rs.getString("customer_id"));
                c.setRewardPoints(rs.getInt("reward_points"));
                c.setIsDeleted(rs.getInt("is_deleted"));
                // Set thêm các trường khác nếu model Customer của bạn có
                list.add(c);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }
}