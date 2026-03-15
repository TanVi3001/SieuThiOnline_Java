package business.sql.prod_inventory;

import business.sql.SqlInterface;
import common.db.DatabaseConnection;
import model.Inventory;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.sql.Date;

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
        try {
            Connection con = DatabaseConnection.getConnection();
            String sql = "SELECT * FROM INVENTORY WHERE is_deleted = 0";

            PreparedStatement pst = con.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                String pId = rs.getString("product_id");
                String sId = rs.getString("store_id");
                int qty = rs.getInt("quantity");
                String unit = rs.getString("unit");
                Date lastUpdated = rs.getDate("last_updated");
                int isDel = rs.getInt("is_deleted");

                Inventory inv = new Inventory(pId, sId, qty, unit, lastUpdated, isDel);
                ketQua.add(inv);
            }
            DatabaseConnection.closeConnection(con);
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
}
