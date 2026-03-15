package business.service; 

import common.db.DatabaseConnection;
import business.sql.prod_inventory.ProductsSql; 
import business.sql.sales_order.OrdersSql;      // DAO cho hóa đơn
import business.sql.sales_order.OrderDetailsSql; // DAO cho chi tiết
import model.Order;
import model.OrderDetail;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class PaymentService {
    
    public static boolean thanhToan(Order hoaDon, List<OrderDetail> dsChiTiet) {
        Connection con = null;
        try {
            con = DatabaseConnection.getConnection();
            // BƯỚC 1: QUAN TRỌNG NHẤT - Tắt tự động lưu để quản lý Transaction
            con.setAutoCommit(false);

            // BƯỚC 2: Lưu Hóa đơn chính
            int resOrder = OrdersSql.getInstance().insertWithConn(con, hoaDon);
            if (resOrder <= 0) throw new SQLException("Lỗi lưu hóa đơn!");

            // BƯỚC 3: Duyệt danh sách sản phẩm trong giỏ hàng
            for (OrderDetail ct : dsChiTiet) {
                // 3.1: Lưu chi tiết hóa đơn
                int resDetail = OrderDetailsSql.getInstance().insertWithConn(con, ct);
                if (resDetail <= 0) throw new SQLException("Lỗi lưu chi tiết hóa đơn!");

                // 3.2: Trừ tồn kho sản phẩm
                // Lấy số lượng hiện tại - số lượng bán
                int resUpdateStock = ProductsSql.getInstance().subtractStockWithConn(con, ct.getProductId(), ct.getQuantity());
                if (resUpdateStock <= 0) throw new SQLException("Lỗi trừ tồn kho cho SP: " + ct.getProductId());
            }

            // BƯỚC 4: Nếu mọi thứ xanh mượt thì "Chốt đơn"
            con.commit();
            System.out.println("✅ Thanh toán hoàn tất! Dữ liệu đã được đồng bộ vào Oracle.");
            return true;

        } catch (Exception e) {
            // BƯỚC 5: Có biến là "Quay xe" ngay lập tức
            try {
                if (con != null) con.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            System.out.println("❌ Thanh toán thất bại: " + e.getMessage());
            return false;
        } finally {
            // Trả lại trạng thái cũ và đóng kết nối
            try {
                if (con != null) {
                    con.setAutoCommit(true);
                    con.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}