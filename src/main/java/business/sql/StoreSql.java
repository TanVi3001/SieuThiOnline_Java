package business.sql;

import common.db.DatabaseConnection;
import model.Store;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class StoreSql implements SqlInterface<Store> {
    private static StoreSql instance;

    private StoreSql() {
    }

    public static StoreSql getInstance() {
        if (instance == null) {
            instance = new StoreSql();
        }
        return instance;
    }

    @Override
    public int insert(Store t) {
        int ketQua = 0;
        try {
            Connection con = DatabaseConnection.getConnection();
            String sql = "INSERT INTO STORES (store_id, email, address, phone_number, is_deleted) VALUES (?, ?, ?, ?, ?)";

            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, t.getStoreId());
            pst.setString(2, t.getEmail());
            pst.setString(3, t.getAddress());
            pst.setString(4, t.getPhoneNumber());
            pst.setInt(5, t.getIsDeleted());

            ketQua = pst.executeUpdate();
            DatabaseConnection.closeConnection(con);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ketQua;
    }

    @Override
    public ArrayList<Store> selectAll() {
        ArrayList<Store> ketQua = new ArrayList<>();
        try {
            Connection con = DatabaseConnection.getConnection();
            // Chỉ lấy các cửa hàng chưa bị xóa (is_deleted = 0)
            String sql = "SELECT * FROM STORES WHERE is_deleted = 0";

            PreparedStatement pst = con.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                Store s = new Store(
                        rs.getString("store_id"),
                        rs.getString("email"),
                        rs.getString("address"),
                        rs.getString("phone_number"),
                        rs.getInt("is_deleted")
                );
                ketQua.add(s);
            }
            DatabaseConnection.closeConnection(con);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ketQua;
    }

    @Override
    public int update(Store t) {
        int ketQua = 0;
        try {
            Connection con = DatabaseConnection.getConnection();
            String sql = "UPDATE STORES SET email = ?, address = ?, phone_number = ?, is_deleted = ? WHERE store_id = ?";

            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, t.getEmail());
            pst.setString(2, t.getAddress());
            pst.setString(3, t.getPhoneNumber());
            pst.setInt(4, t.getIsDeleted());
            pst.setString(5, t.getStoreId());

            ketQua = pst.executeUpdate();
            DatabaseConnection.closeConnection(con);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ketQua;
    }

    @Override
    public int delete(String id) {
        int ketQua = 0;
        try {
            Connection con = DatabaseConnection.getConnection();
            // Dùng "Xóa mềm" (Soft Delete) - Chuyển trạng thái is_deleted thành 1 thay vì xóa hẳn khỏi DB
            String sql = "UPDATE STORES SET is_deleted = 1 WHERE store_id = ?";

            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, id);

            ketQua = pst.executeUpdate();
            DatabaseConnection.closeConnection(con);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ketQua;
    }

    @Override
    public Store selectById(String id) {
        Store ketQua = null;
        try {
            Connection con = DatabaseConnection.getConnection();
            String sql = "SELECT * FROM STORES WHERE store_id = ?";

            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, id);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                ketQua = new Store(
                        rs.getString("store_id"),
                        rs.getString("email"),
                        rs.getString("address"),
                        rs.getString("phone_number"),
                        rs.getInt("is_deleted")
                );
            }
            DatabaseConnection.closeConnection(con);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ketQua;
    }
}
