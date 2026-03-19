package business.sql.prod_inventory;

import common.db.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ProductsSql {
    private static ProductsSql instance;

    private ProductsSql() {}

    public static ProductsSql getInstance() {
        if (instance == null) {
            instance = new ProductsSql();
        }
        return instance;
    }

    /**
     * Hàm "át chủ bài": Trừ kho trực tiếp trong Transaction
     * Leader Vĩ: Đã fix - Chuyển hướng sang bảng INVENTORY cho khớp Database
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
}