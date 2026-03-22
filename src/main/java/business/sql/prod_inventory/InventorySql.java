package business.sql.prod_inventory;

import business.sql.SqlInterface;
import common.db.DatabaseConnection;
import model.product.Inventory;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.sql.Date;
import java.util.List;

public class InventorySql implements SqlInterface<Inventory> {

    private static InventorySql instance;

    private InventorySql() {
    }

    public static InventorySql getInstance() {
        if (instance == null) {
            instance = new InventorySql();
        }
        return instance;
    }

    @Override
    public int insert(Inventory t) {
        int ketQua = 0;
        try {
            Connection con = DatabaseConnection.getConnection();
            String sql = "INSERT INTO INVENTORY (product_id, store_id, quantity, unit, last_updated, is_deleted) VALUES (?, ?, ?, ?, ?, ?)";

            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, t.getProductId());
            pst.setString(2, t.getStoreId());
            pst.setInt(3, t.getQuantity());
            pst.setString(4, t.getUnit());
            pst.setDate(5, t.getLastUpdated());
            pst.setInt(6, t.getIsDeleted());

            ketQua = pst.executeUpdate();
            DatabaseConnection.closeConnection(con);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ketQua;
    }

    @Override
    public ArrayList<Inventory> selectAll() {
        ArrayList<Inventory> ketQua = new ArrayList<>();
        String sql = "SELECT * FROM INVENTORY WHERE is_deleted = 0";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                Inventory inv = new Inventory();
                inv.setProductId(rs.getString("product_id"));
                inv.setStoreId(rs.getString("store_id"));
                inv.setQuantity(rs.getInt("quantity"));
                inv.setUnit(rs.getString("unit"));
                inv.setLastUpdated(rs.getDate("last_updated"));
                inv.setIsDeleted(rs.getInt("is_deleted"));
                ketQua.add(inv);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ketQua;
    }

    @Override
    public int update(Inventory t) {
        int ketQua = 0;
        try {
            Connection con = DatabaseConnection.getConnection();
            // Cập nhật dựa trên cả 2 khóa chính
            String sql = "UPDATE INVENTORY SET quantity = ?, unit = ?, last_updated = ?, is_deleted = ? WHERE product_id = ? AND store_id = ?";

            PreparedStatement pst = con.prepareStatement(sql);
            pst.setInt(1, t.getQuantity());
            pst.setString(2, t.getUnit());
            pst.setDate(3, t.getLastUpdated());
            pst.setInt(4, t.getIsDeleted());
            pst.setString(5, t.getProductId());
            pst.setString(6, t.getStoreId());

            ketQua = pst.executeUpdate();
            DatabaseConnection.closeConnection(con);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ketQua;
    }

    // ==============================================================================
    // LƯU Ý: 2 hàm dưới đây ghi đè từ Interface nhưng KHÔNG DÙNG ĐƯỢC cho bảng này
    // vì bảng INVENTORY có tận 2 khóa chính (Composite Key).
    // ==============================================================================
    @Override
    public int delete(String id) {
        return 0;
    }

    @Override
    public Inventory selectById(String id) {
        return null;
    }

    // ==============================================================================
    // ĐÂY LÀ 2 HÀM TẠO THÊM ĐỂ XỬ LÝ RIÊNG CHO BẢNG KHÓA KÉP NÀY
    // ==============================================================================
    // Hàm xóa mềm dựa trên cả Mã SP và Mã Cửa Hàng
    public int deleteByCompositeKey(String productId, String storeId) {
        int ketQua = 0;
        try {
            Connection con = DatabaseConnection.getConnection();
            String sql = "UPDATE INVENTORY SET is_deleted = 1 WHERE product_id = ? AND store_id = ?";

            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, productId);
            pst.setString(2, storeId);

            ketQua = pst.executeUpdate();
            DatabaseConnection.closeConnection(con);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ketQua;
    }

    // Hàm tìm kiếm dựa trên cả Mã SP và Mã Cửa Hàng
    public Inventory selectByCompositeKey(String productId, String storeId) {
        Inventory ketQua = null;
        try {
            Connection con = DatabaseConnection.getConnection();
            String sql = "SELECT * FROM INVENTORY WHERE product_id = ? AND store_id = ?";

            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, productId);
            pst.setString(2, storeId);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                String pId = rs.getString("product_id");
                String sId = rs.getString("store_id");
                int qty = rs.getInt("quantity");
                String unit = rs.getString("unit");
                Date lastUpdated = rs.getDate("last_updated");
                int isDel = rs.getInt("is_deleted");

                ketQua = new Inventory(pId, sId, qty, unit, lastUpdated, isDel);
            }
            DatabaseConnection.closeConnection(con);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ketQua;
    }

    @Override
    public List<Inventory> selectByCondition(String condition) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
    public int subtractStock(Connection con, String productId, int quantity) throws SQLException {
        // SQL: Trừ số lượng và cập nhật ngày giờ mới nhất
        // Điều kiện: quantity >= ? để đảm bảo không bị trừ âm kho
        String sql = "UPDATE INVENTORY SET quantity = quantity - ?, last_updated = CURRENT_TIMESTAMP "
                   + "WHERE product_id = ? AND quantity >= ?";

        try (PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setInt(1, quantity);
            pst.setString(2, productId);
            pst.setInt(3, quantity);

            int rowsAffected = pst.executeUpdate();

            if (rowsAffected == 0) {
                // Bắn lỗi ra để Service biết và thực hiện ROLLBACK
                throw new SQLException("LỖI: Sản phẩm mã " + productId + " không đủ số lượng tồn kho!");
            }
            return rowsAffected;
        }
    }   
}
