package business.sql.sales_order;

import business.sql.SqlInterface;
import common.db.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.order.Order;

public class OrdersSql implements SqlInterface<Order> {
    private static OrdersSql instance;

    private OrdersSql() {
    }

    public static OrdersSql getInstance() {
        if (instance == null) {
            instance = new OrdersSql();
        }
        return instance;
    }

    public int insertWithConn(Connection con, Order order) throws SQLException {
        String sql = "INSERT INTO ORDERS (order_id, customer_id, employee_id, order_date, total_amount, status) "
                + "VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setString(1, order.getOrderId());
            pst.setString(2, order.getCustomerId());
            pst.setString(3, order.getEmployeeId());
            pst.setDate(4, order.getOrderDate());
            pst.setDouble(5, order.getTotalAmount());
            pst.setString(6, order.getStatus());
            return pst.executeUpdate();
        }
    }

    @Override
    public int insert(Order t) {
        try (Connection con = DatabaseConnection.getConnection()) {
            return insertWithConn(con, t);
        } catch (SQLException e) {
            System.err.println("Loi tai OrdersSql.insert: " + e.getMessage());
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int update(Order t) {
        String sql = "UPDATE ORDERS "
                + "SET customer_id = ?, employee_id = ?, order_date = ?, total_amount = ?, status = ? "
                + "WHERE order_id = ? AND NVL(is_deleted, 0) = 0";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setString(1, t.getCustomerId());
            pst.setString(2, t.getEmployeeId());
            pst.setDate(3, t.getOrderDate());
            pst.setDouble(4, t.getTotalAmount());
            pst.setString(5, t.getStatus());
            pst.setString(6, t.getOrderId());
            return pst.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Loi tai OrdersSql.update: " + e.getMessage());
            e.printStackTrace();
            return 0;
        }
    }

    public int updateStatus(String orderId, String status) {
        String sql = "UPDATE ORDERS SET status = ? WHERE order_id = ? AND NVL(is_deleted, 0) = 0";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setString(1, status);
            pst.setString(2, orderId);
            return pst.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Loi tai OrdersSql.updateStatus: " + e.getMessage());
            e.printStackTrace();
            return 0;
        }
    }

    public int updateStatusWithConn(Connection con, String orderId, String status) throws SQLException {
        String sql = "UPDATE ORDERS SET status = ? WHERE order_id = ? AND NVL(is_deleted, 0) = 0";
        try (PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setString(1, status);
            pst.setString(2, orderId);
            return pst.executeUpdate();
        }
    }

    @Override
    public int delete(String id) {
        String sql = "UPDATE ORDERS SET is_deleted = 1 WHERE order_id = ?";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setString(1, id);
            return pst.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Loi tai OrdersSql.delete: " + e.getMessage());
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public Order selectById(String id) {
        String sql = "SELECT * FROM ORDERS WHERE order_id = ? AND NVL(is_deleted, 0) = 0";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setString(1, id);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    return mapOrder(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Loi tai OrdersSql.selectById: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Order> selectByCondition(String condition) {
        ArrayList<Order> list = new ArrayList<>();
        String sql = "SELECT * FROM ORDERS "
                + "WHERE NVL(is_deleted, 0) = 0 AND UPPER(status) = UPPER(?) "
                + "ORDER BY order_date DESC";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setString(1, condition);
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    list.add(mapOrder(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Loi tai OrdersSql.selectByCondition: " + e.getMessage());
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public ArrayList<Order> selectAll() {
        ArrayList<Order> list = new ArrayList<>();
        String sql = "SELECT * FROM ORDERS WHERE NVL(is_deleted, 0) = 0 ORDER BY order_date DESC";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                list.add(mapOrder(rs));
            }
        } catch (SQLException e) {
            System.err.println("Loi tai OrdersSql.selectAll: " + e.getMessage());
            e.printStackTrace();
        }
        return list;
    }

    private Order mapOrder(ResultSet rs) throws SQLException {
        String id = rs.getString("order_id");
        String customerId = rs.getString("customer_id");
        String employeeId = rs.getString("employee_id");
        java.sql.Date date = rs.getDate("order_date");
        double amount = rs.getDouble("total_amount");
        String status = rs.getString("status");
        boolean deleted = rs.getInt("is_deleted") == 1;
        return new Order(id, customerId, employeeId, date, amount, status, deleted);
    }
}
