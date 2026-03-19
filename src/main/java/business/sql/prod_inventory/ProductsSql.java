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
}
