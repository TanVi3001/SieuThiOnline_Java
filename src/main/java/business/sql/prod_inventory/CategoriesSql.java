package business.sql.prod_inventory;

import common.db.DatabaseConnection;
import model.Category;
import business.sql.SqlInterface; // Import interface từ package gốc
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CategoriesSql implements SqlInterface<Category> {

    // Áp dụng mẫu Singleton
    public static CategoriesSql getInstance() {
        return new CategoriesSql();
    }

    @Override
    public int insert(Category t) {
        int ketQua = 0;
        String sql = "INSERT INTO CATEGORIES (category_id, category_name, description, is_deleted) VALUES (?, ?, ?, ?)";
        
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {
            
            pst.setString(1, t.getCategoryId());
            pst.setString(2, t.getCategoryName());
            pst.setString(3, t.getDescription());
            pst.setInt(4, t.getIsDeleted());
            
            ketQua = pst.executeUpdate();
            DatabaseConnection.closeConnection(con);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ketQua;
    }

    @Override
    public ArrayList<Category> selectAll() {
        ArrayList<Category> ketQua = new ArrayList<>();
        String sql = "SELECT * FROM CATEGORIES WHERE is_deleted = 0";
        
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {
            
            while (rs.next()) {
                Category c = new Category(
                    rs.getString("category_id"),
                    rs.getString("category_name"),
                    rs.getString("description"),
                    rs.getInt("is_deleted")
                );
                ketQua.add(c);
            }
            DatabaseConnection.closeConnection(con);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ketQua;
    }

    @Override
    public int update(Category t) { 
        int ketQua = 0;
        String sql = "UPDATE CATEGORIES SET category_name = ?, description = ?, is_deleted = ? WHERE category_id = ?";
        
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {
            
            pst.setString(1, t.getCategoryName());
            pst.setString(2, t.getDescription());
            pst.setInt(3, t.getIsDeleted());
            pst.setString(4, t.getCategoryId());
            
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
        String sql = "UPDATE CATEGORIES SET is_deleted = 1 WHERE category_id = ?";
        
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
    public Category selectById(String id) { 
        Category ketQua = null;
        String sql = "SELECT * FROM CATEGORIES WHERE category_id = ?";
        
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {
            
            pst.setString(1, id);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    ketQua = new Category(
                        rs.getString("category_id"),
                        rs.getString("category_name"),
                        rs.getString("description"),
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

    @Override
    public List<Category> selectByCondition(String condition) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}