package business.sql;

import common.db.DatabaseConnection;
import model.Category;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CategorySql implements SqlInterface<Category> {
    // 1. Khai báo biến static instance đúng kiểu CategorySql

    private static CategorySql instance;

    // 2. Private constructor để "khóa" việc dùng lệnh 'new' bừa bãi
    private CategorySql() {
    }

    // 3. Hàm getInstance trả về duy nhất 1 instance
    public static CategorySql getInstance() {
        if (instance == null) {
            instance = new CategorySql();
        }
        return instance;
    }

    @Override
    public int insert(Category t) {
        int ketQua = 0;
        try {
            Connection con = DatabaseConnection.getConnection();
            String sql = "INSERT INTO CATEGORIES (category_id, category_name, description, is_deleted) VALUES (?, ?, ?, ?)";

            PreparedStatement pst = con.prepareStatement(sql);
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
        try {
            Connection con = DatabaseConnection.getConnection();
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
            DatabaseConnection.closeConnection(con);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ketQua;
    }

    @Override
    public int update(Category t) {
        int ketQua = 0;
        try {
            Connection con = DatabaseConnection.getConnection();
            String sql = "UPDATE CATEGORIES SET category_name = ?, description = ?, is_deleted = ? WHERE category_id = ?";

            PreparedStatement pst = con.prepareStatement(sql);
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
        try {
            Connection con = DatabaseConnection.getConnection();
            // Xóa mềm: Chuyển is_deleted = 1
            String sql = "UPDATE CATEGORIES SET is_deleted = 1 WHERE category_id = ?";

            PreparedStatement pst = con.prepareStatement(sql);
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
        try {
            Connection con = DatabaseConnection.getConnection();
            String sql = "SELECT * FROM CATEGORIES WHERE category_id = ?";

            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, id);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                String catId = rs.getString("category_id");
                String name = rs.getString("category_name");
                String desc = rs.getString("description");
                int isDel = rs.getInt("is_deleted");

                ketQua = new Category(catId, name, desc, isDel);
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
