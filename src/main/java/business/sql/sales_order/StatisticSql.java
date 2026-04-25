package business.sql.sales_order;

import common.db.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StatisticSql {

    private static StatisticSql instance;

    private StatisticSql() {
    }

    public static StatisticSql getInstance() {
        if (instance == null) {
            instance = new StatisticSql();
        }
        return instance;
    }

    public int getTotalCustomers() {
        String sql = "SELECT COUNT(*) FROM CUSTOMERS WHERE NVL(is_deleted, 0) = 0";
        return queryInt(sql);
    }

    public int getTotalProducts() {
        String sql = "SELECT COUNT(*) FROM PRODUCTS WHERE NVL(is_deleted, 0) = 0";
        return queryInt(sql);
    }

    public int getTotalOrders() {
        String sql = "SELECT COUNT(*) FROM ORDERS WHERE NVL(is_deleted, 0) = 0";
        return queryInt(sql);
    }

    public int getTodayOrders() {
        String sql = "SELECT COUNT(*) FROM ORDERS "
                + "WHERE NVL(is_deleted, 0) = 0 AND TRUNC(order_date) = TRUNC(SYSDATE)";
        return queryInt(sql);
    }

    public double getMonthlyRevenue() {
        String sql = "SELECT NVL(SUM(total_amount), 0) FROM ORDERS "
                + "WHERE NVL(is_deleted, 0) = 0 "
                + "AND UPPER(NVL(status, '')) <> 'CANCELLED' "
                + "AND order_date >= TRUNC(SYSDATE, 'MM') "
                + "AND order_date < ADD_MONTHS(TRUNC(SYSDATE, 'MM'), 1)";
        return queryDouble(sql);
    }

    public List<Map<String, Object>> getBestSellingProducts(int limit) {
        List<Map<String, Object>> rows = new ArrayList<>();
        String sql = "SELECT * FROM ("
                + "SELECT od.product_id, NVL(p.product_name, od.product_id) AS product_name, "
                + "       SUM(od.quantity) AS total_sold, "
                + "       SUM(od.quantity * od.unit_price) AS total_revenue "
                + "FROM ORDER_DETAILS od "
                + "JOIN ORDERS o ON od.order_id = o.order_id "
                + "LEFT JOIN PRODUCTS p ON od.product_id = p.product_id "
                + "WHERE NVL(od.is_deleted, 0) = 0 "
                + "AND NVL(o.is_deleted, 0) = 0 "
                + "AND UPPER(NVL(o.status, '')) <> 'CANCELLED' "
                + "GROUP BY od.product_id, p.product_name "
                + "ORDER BY total_sold DESC, total_revenue DESC"
                + ") WHERE ROWNUM <= ?";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setInt(1, limit);
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> row = new HashMap<>();
                    row.put("product_id", rs.getString("product_id"));
                    row.put("product_name", rs.getString("product_name"));
                    row.put("total_sold", rs.getInt("total_sold"));
                    row.put("total_revenue", rs.getDouble("total_revenue"));
                    rows.add(row);
                }
            }
        } catch (SQLException e) {
            System.err.println("Loi tai StatisticSql.getBestSellingProducts: " + e.getMessage());
            e.printStackTrace();
        }
        return rows;
    }

    public List<Map<String, Object>> getRecentOrders(int limit) {
        List<Map<String, Object>> rows = new ArrayList<>();
        String sql = "SELECT * FROM ("
                + "SELECT o.order_id, NVL(c.customer_name, 'Khách vãng lai') AS customer_name, "
                + "       o.total_amount, o.status, o.order_date "
                + "FROM ORDERS o "
                + "LEFT JOIN CUSTOMERS c ON o.customer_id = c.customer_id "
                + "WHERE NVL(o.is_deleted, 0) = 0 "
                + "ORDER BY o.order_date DESC, o.order_id DESC"
                + ") WHERE ROWNUM <= ?";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setInt(1, limit);
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> row = new HashMap<>();
                    row.put("order_id", rs.getString("order_id"));
                    row.put("customer_name", rs.getString("customer_name"));
                    row.put("total_amount", rs.getDouble("total_amount"));
                    row.put("status", rs.getString("status"));
                    row.put("order_date", rs.getDate("order_date"));
                    rows.add(row);
                }
            }
        } catch (SQLException e) {
            System.err.println("Loi tai StatisticSql.getRecentOrders: " + e.getMessage());
            e.printStackTrace();
        }
        return rows;
    }

    private int queryInt(String sql) {
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Loi truy van thong ke: " + e.getMessage());
            e.printStackTrace();
        }
        return 0;
    }

    private double queryDouble(String sql) {
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {
            if (rs.next()) {
                return rs.getDouble(1);
            }
        } catch (SQLException e) {
            System.err.println("Loi truy van thong ke: " + e.getMessage());
            e.printStackTrace();
        }
        return 0;
    }
}
