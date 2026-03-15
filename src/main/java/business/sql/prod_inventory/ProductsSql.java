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
        // SQL: Giảm số lượng trong bảng INVENTORY (vì bảng PRODUCTS không giữ số lượng)
        String sql = "UPDATE INVENTORY SET quantity = quantity - ? WHERE product_id = ?";
        
        try (PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setInt(1, quantity);
            pst.setString(2, productId);
            
            int res = pst.executeUpdate();
            
            // Nếu không tìm thấy sản phẩm trong kho để trừ
            if (res == 0) {
                throw new SQLException("LỖI: Mã sản phẩm " + productId + " không tồn tại trong bảng INVENTORY!");
            }
            
            return res;
        }
    }
}