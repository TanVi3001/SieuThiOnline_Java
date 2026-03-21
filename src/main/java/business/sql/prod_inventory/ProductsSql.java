package business.sql.prod_inventory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import model.product.Product;

public class ProductsSql {

    private static ProductsSql instance;

    private ProductsSql() {
    }

    public static ProductsSql getInstance() {
        if (instance == null) {
            instance = new ProductsSql();
        }
        return instance;
    }

    /**
     * Hàm "át chủ bài": Trừ kho trực tiếp trong Transaction Leader Vĩ: Đã fix -
     * Chuyển hướng sang bảng INVENTORY cho khớp Database
     *
     * @param con
     * @param productId
     * @param quantity
     * @return
     * @throws java.sql.SQLException
     */
    public int subtractStockWithConn(Connection con, String productId, int quantity) throws SQLException {
        // SQL: Chỉ trừ nếu số lượng tồn kho (quantity) lớn hơn hoặc bằng số lượng mua
        String sql = "UPDATE INVENTORY SET quantity = quantity - ? WHERE product_id = ? AND quantity >= ?";

        try (PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setInt(1, quantity);
            pst.setString(2, productId);
            pst.setInt(3, quantity); // Ràng buộc: Kho phải còn đủ hàng mới cho trừ

            int res = pst.executeUpdate();

            if (res == 0) {
                // Lỗi này sẽ giúp PaymentService biết để Rollback toàn bộ hóa đơn
                throw new SQLException("LỖI: Sản phẩm " + productId + " không đủ hàng trong kho hoặc mã không tồn tại!");
            }

            return res;
        }
    }

    public List<Product> selectAll() {
        java.util.List<model.product.Product> list = new java.util.ArrayList<>();
        // Câu SQL lấy thông tin sản phẩm và số lượng từ kho
        String sql = "SELECT p.product_id, p.product_name, p.base_price, i.quantity "
                + "FROM PRODUCTS p JOIN INVENTORY i ON p.product_id = i.product_id "
                + "WHERE p.is_deleted = 0";

        try (java.sql.Connection con = common.db.DatabaseConnection.getConnection(); java.sql.PreparedStatement ps = con.prepareStatement(sql); java.sql.ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                model.product.Product p = new model.product.Product();
                p.setProductId(rs.getString("product_id"));
                p.setProductName(rs.getString("product_name"));
                p.setBasePrice(rs.getBigDecimal("base_price"));
                p.setQuantity(rs.getInt("quantity"));
                list.add(p);
            }
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    public boolean insert(Product p) {
        String sqlProduct = "INSERT INTO PRODUCTS (product_id, product_name, base_price, is_deleted) VALUES (?, ?, ?, 0)";
        String sqlInventory = "INSERT INTO INVENTORY (product_id, quantity) VALUES (?, ?)";

        try (Connection con = common.db.DatabaseConnection.getConnection()) {
            con.setAutoCommit(false); // Bắt đầu Transaction

            try (PreparedStatement psProd = con.prepareStatement(sqlProduct);
                 PreparedStatement psInv = con.prepareStatement(sqlInventory)) {

                // 1. Thêm vào bảng PRODUCTS
                psProd.setString(1, p.getProductId());
                psProd.setString(2, p.getProductName());
                psProd.setBigDecimal(3, p.getBasePrice());
                psProd.executeUpdate();

                // 2. Thêm vào bảng INVENTORY (khởi tạo số lượng)
                psInv.setString(1, p.getProductId());
                psInv.setInt(2, p.getQuantity()); // Số lượng ban đầu
                psInv.executeUpdate();

                con.commit(); // Thành công cả hai thì commit
                return true;
            } catch (SQLException e) {
                con.rollback(); // Lỗi một trong hai thì rollback
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public boolean update(Product p) {
        String sql = "UPDATE PRODUCTS SET product_name = ?, base_price = ? WHERE product_id = ? AND is_deleted = 0";

        try (Connection con = common.db.DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, p.getProductName());
            ps.setBigDecimal(2, p.getBasePrice());
            ps.setString(3, p.getProductId());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public boolean delete(String productId) {
        // Chuyển trạng thái is_deleted thành 1
        String sql = "UPDATE PRODUCTS SET is_deleted = 1 WHERE product_id = ?";

        try (Connection con = common.db.DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, productId);

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
