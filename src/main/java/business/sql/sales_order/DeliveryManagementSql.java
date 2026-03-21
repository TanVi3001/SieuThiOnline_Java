package business.sql.sales_order;

import business.sql.SqlInterface;
import common.db.DatabaseConnection;
import model.order.DeliveryManagement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class DeliveryManagementSql implements SqlInterface<DeliveryManagement> {

    private static DeliveryManagementSql instance;

    private DeliveryManagementSql() {
    }

    public static DeliveryManagementSql getInstance() {
        if (instance == null) {
            instance = new DeliveryManagementSql();
        }
        return instance;
    }

    @Override
    public int insert(DeliveryManagement t) {
        int ketQua = 0;
        try {
            Connection con = DatabaseConnection.getConnection();
            String sql = "INSERT INTO DELIVERY_MANAGEMENT (delivery_id, order_id, employee_id, execution_date, status, is_deleted) VALUES (?, ?, ?, ?, ?, ?)";

            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, t.getDeliveryId());
            pst.setString(2, t.getOrderId());
            pst.setString(3, t.getEmployeeId());
            pst.setDate(4, t.getExecutionDate());
            pst.setString(5, t.getStatus());
            pst.setInt(6, t.getIsDeleted());

            ketQua = pst.executeUpdate();
            DatabaseConnection.closeConnection(con);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ketQua;
    }

    @Override
    public int update(DeliveryManagement t) {
        int ketQua = 0;
        try {
            java.sql.Connection con = common.db.DatabaseConnection.getConnection();
            String sql = "UPDATE DELIVERY_MANAGEMENT SET order_id=?, employee_id=?, execution_date=?, status=?, is_deleted=? WHERE delivery_id=?";
            java.sql.PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, t.getOrderId());
            pst.setString(2, t.getEmployeeId());
            pst.setDate(3, t.getExecutionDate());
            pst.setString(4, t.getStatus());
            pst.setInt(5, t.getIsDeleted());
            pst.setString(6, t.getDeliveryId());

            ketQua = pst.executeUpdate();
            common.db.DatabaseConnection.closeConnection(con);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ketQua;
    }

    @Override
    public int delete(String id) {
        int ketQua = 0;
        try {
            java.sql.Connection con = common.db.DatabaseConnection.getConnection();
            String sql = "UPDATE DELIVERY_MANAGEMENT SET is_deleted = 1 WHERE delivery_id = ?";
            java.sql.PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, id);
            ketQua = pst.executeUpdate();
            common.db.DatabaseConnection.closeConnection(con);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ketQua;
    }

    @Override
    public ArrayList<DeliveryManagement> selectAll() {
        ArrayList<DeliveryManagement> ketQua = new ArrayList<>();
        String sql = "SELECT * FROM DELIVERY_MANAGEMENT WHERE is_deleted = 0";

        try (Connection con = common.db.DatabaseConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                DeliveryManagement dm = new DeliveryManagement();

                // Gán dữ liệu bằng các hàm set để tránh phụ thuộc vào thứ tự tham số Constructor
                dm.setDeliveryId(rs.getString("delivery_id"));
                dm.setOrderId(rs.getString("order_id"));
                dm.setEmployeeId(rs.getString("employee_id"));
                dm.setExecutionDate(rs.getDate("execution_date"));
                dm.setStatus(rs.getString("status"));
                dm.setIsDeleted(rs.getInt("is_deleted"));

                ketQua.add(dm);
            }
        } catch (Exception e) {
            // In lỗi chi tiết để dễ debug trong quá trình làm đồ án
            System.err.println("Lỗi tại DeliveryManagementSql.selectAll: " + e.getMessage());
            e.printStackTrace();
        }
        return ketQua;
    }

    @Override
    public DeliveryManagement selectById(String id) {
        DeliveryManagement ketQua = null;
        try {
            java.sql.Connection con = common.db.DatabaseConnection.getConnection();
            // Vẫn phải check is_deleted = 0 để đảm bảo không tìm thấy thằng đã bị "xóa"
            String sql = "SELECT * FROM DELIVERY_MANAGEMENT WHERE delivery_id = ? AND is_deleted = 0";
            java.sql.PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, id);

            java.sql.ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                ketQua = new DeliveryManagement(
                        rs.getString("delivery_id"),
                        rs.getString("order_id"),
                        rs.getString("employee_id"),
                        rs.getDate("execution_date"),
                        rs.getString("status"),
                        rs.getInt("is_deleted")
                );
            }
            common.db.DatabaseConnection.closeConnection(con);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ketQua;
    }

    @Override
    public List<DeliveryManagement> selectByCondition(String condition) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
