package com.mycompany.sieuthionline.dao;

import com.mycompany.sieuthionline.database.JDBCUtil;
import com.mycompany.sieuthionline.model.Category;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class CategoryDAO implements DAOInterface<Category> {

    // Áp dụng mẫu Singleton (Chỉ tạo 1 instance duy nhất)
    public static CategoryDAO getInstance() {
        return new CategoryDAO();
    }

    @Override
    public int insert(Category t) {
        int ketQua = 0;
        try {
            Connection con = JDBCUtil.getConnection();
            String sql = "INSERT INTO CATEGORIES (category_id, category_name, description, is_deleted) VALUES (?, ?, ?, ?)";
            
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, t.getCategoryId());
            pst.setString(2, t.getCategoryName());
            pst.setString(3, t.getDescription());
            pst.setInt(4, t.getIsDeleted());
            
            ketQua = pst.executeUpdate();
            
            JDBCUtil.closeConnection(con);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ketQua;
    }

    @Override
    public ArrayList<Category> selectAll() {
        ArrayList<Category> ketQua = new ArrayList<>();
        try {
            Connection con = JDBCUtil.getConnection();
            // Lấy những danh mục chưa bị xóa (is_deleted = 0)
            String sql = "SELECT * FROM CATEGORIES WHERE is_deleted = 0";
            
            PreparedStatement pst = con.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            
            while (rs.next()) {
                String id = rs.getString("category_id");
                String name = rs.getString("category_name");
                String desc = rs.getString("description");
                int isDel = rs.getInt("is_deleted");
                
                Category c = new Category(id, name, desc, isDel);
                ketQua.add(c);
            }
            JDBCUtil.closeConnection(con);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ketQua;
    }

    // Các hàm update, delete, selectById bạn tạo sườn trống return 0 hoặc null trước
    @Override
    public int update(Category t) { return 0; }

    @Override
    public int delete(String id) { return 0; }

    @Override
    public Category selectById(String id) { return null; }
}
