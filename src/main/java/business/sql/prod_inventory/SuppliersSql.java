package business.sql.prod_inventory;

import common.db.DatabaseConnection;
import model.Supplier;
import business.sql.SqlInterface;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class SuppliersSql implements SqlInterface<Supplier> {

    // Áp dụng Singleton Pattern
    public static SuppliersSql getInstance() {
        return new SuppliersSql();
    }

    @Override
    public int insert(Supplier t) {
        int ketQua = 0;
        String sql = "INSERT INTO SUPPLIERS (supplier_id, supplier_name, email, address, phone_number, is_deleted) VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {
            
            pst.setString(1, t.getSupplierId());
            pst.setString(2, t.getSupplierName());
            pst.setString(3, t.getEmail());
            pst.setString(4, t.getAddress());
            pst.setString(5, t.getPhoneNumber());
            pst.setInt(6, t.getIsDeleted());
            
            ketQua = pst.executeUpdate();
            DatabaseConnection.closeConnection(con);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ketQua;
    }

    @Override
    public ArrayList<Supplier> selectAll() {
        ArrayList<Supplier> ketQua = new ArrayList<>();
        String sql = "SELECT * FROM SUPPLIERS WHERE is_deleted = 0";
        
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {
            
            while (rs.next()) {
                Supplier s = new Supplier(
                    rs.getString("supplier_id"),
                    rs.getString("supplier_name"),
                    rs.getString("email"),
                    rs.getString("address"),
                    rs.getString("phone_number"),
                    rs.getInt("is_deleted")
                );
                ketQua.add(s);
            }
            DatabaseConnection.closeConnection(con);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ketQua;
    }

    @Override
    public int update(Supplier t) { 
        int ketQua = 0;
        String sql = "UPDATE SUPPLIERS SET supplier_name = ?, email = ?, address = ?, phone_number = ?, is_deleted = ? WHERE supplier_id = ?";
        
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {
            
            pst.setString(1, t.getSupplierName());
            pst.setString(2, t.getEmail());
            pst.setString(3, t.getAddress());
            pst.setString(4, t.getPhoneNumber());
            pst.setInt(5, t.getIsDeleted());
            pst.setString(6, t.getSupplierId());
            
            ketQua = pst.executeUpdate();
            DatabaseConnection.closeConnection(con);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ketQua;
    }

    @Override
    public int delete(String id) { 
        int ketQua = 0;
        String sql = "UPDATE SUPPLIERS SET is_deleted = 1 WHERE supplier_id = ?";
        
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {
            
            pst.setString(1, id);
            ketQua = pst.executeUpdate();
            DatabaseConnection.closeConnection(con);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ketQua;
    }

    @Override
    public Supplier selectById(String id) { 
        Supplier ketQua = null;
        String sql = "SELECT * FROM SUPPLIERS WHERE supplier_id = ?";
        
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {
            
            pst.setString(1, id);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    ketQua = new Supplier(
                        rs.getString("supplier_id"),
                        rs.getString("supplier_name"),
                        rs.getString("email"),
                        rs.getString("address"),
                        rs.getString("phone_number"),
                        rs.getInt("is_deleted")
                    );
                }
            }
            DatabaseConnection.closeConnection(con);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ketQua;
    }
}