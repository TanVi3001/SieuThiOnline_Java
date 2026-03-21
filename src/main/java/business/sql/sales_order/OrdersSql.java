package business.sql.sales_order;

import common.db.DatabaseConnection;
import model.order.Order;
import business.sql.SqlInterface;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrdersSql implements SqlInterface<Order> {
    private static OrdersSql instance;

    private OrdersSql() {}

    public static OrdersSql getInstance() {
        if (instance == null) {
            instance = new OrdersSql();
        }
        return instance;
    }

    // Hàm quan trọng dùng cho PaymentService
    public int insertWithConn(Connection con, Order hoaDon) throws SQLException {
        String sql = "INSERT INTO ORDERS (order_id, customer_id, employee_id, order_date, total_amount, status) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setString(1, hoaDon.getOrderId());
            pst.setString(2, hoaDon.getCustomerId());
            pst.setString(3, hoaDon.getEmployeeId());
            pst.setDate(4, hoaDon.getOrderDate());
            pst.setDouble(5, hoaDon.getTotalAmount());
            pst.setString(6, hoaDon.getStatus());

            return pst.executeUpdate();
        }
    }

    @Override
    public int insert(Order t) {
        try (Connection con = DatabaseConnection.getConnection()) {
            return insertWithConn(con, t);
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override public int update(Order t) { return 0; }
    @Override public int delete(String id) { return 0; }
    @Override public Order selectById(String id) { return null; }

    @Override
    public List<Order> selectByCondition(String condition) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    @Override
    public ArrayList<Order> selectAll() {
        ArrayList<Order> list = new ArrayList<>();
        // Thường hóa đơn sẽ sắp xếp theo ngày mới nhất lên đầu
        String sql = "SELECT * FROM ORDERS ORDER BY order_date DESC";

        try (Connection con = common.db.DatabaseConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                // Lấy dữ liệu từ ResultSet
                String id = rs.getString("order_id");
                String cusId = rs.getString("customer_id");
                String empId = rs.getString("employee_id");
                java.sql.Date date = rs.getDate("order_date");
                double amount = rs.getDouble("total_amount");
                String status = rs.getString("status");

                // Khởi tạo Object bằng Constructor đầy đủ tham số mà bạn vừa gửi
                Order hoaDon = new Order(id, cusId, empId, date, amount, status);

                list.add(hoaDon);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi tại OrdersSql.selectAll: " + e.getMessage());
            e.printStackTrace();
        }
        return list;
    }
}