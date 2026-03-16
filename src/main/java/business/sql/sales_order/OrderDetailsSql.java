package business.sql.sales_order;

import common.db.DatabaseConnection;
import model.order.OrderDetail;
import business.sql.SqlInterface;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * OrderDetailsSql: Xử lý lưu chi tiết hóa đơn vào Oracle Leader Vĩ: Đã fix
 * Singleton và implement logic Transaction
 */
public class OrderDetailsSql implements SqlInterface<OrderDetail> {

    private static OrderDetailsSql instance;

    private OrderDetailsSql() {
    }

    // Fix Singleton: Đảm bảo chỉ có 1 thực thể duy nhất
    public static OrderDetailsSql getInstance() {
        if (instance == null) {
            instance = new OrderDetailsSql();
        }
        return instance;
    }

    /**
     * Hàm cực kỳ quan trọng: Lưu vào DB bằng Connection dùng chung
     * (Transaction) Dùng cho PaymentService để Rollback nếu có lỗi
     *
     * @param con
     * @param ct
     * @return
     * @throws java.sql.SQLException
     */
    public int insertWithConn(Connection con, OrderDetail ct) throws SQLException {
    // FIX: Thêm cột order_detail_id vào câu lệnh SQL
    String sql = "INSERT INTO ORDER_DETAILS (order_detail_id, order_id, product_id, quantity, unit_price) VALUES (?, ?, ?, ?, ?)";

    try (PreparedStatement pst = con.prepareStatement(sql)) {
        // Tự động tạo một cái mã ID ngẫu nhiên (ví dụ: DET-12345...)
        String randomId = "DET-" + java.util.UUID.randomUUID().toString().substring(0, 8);
        
        pst.setString(1, randomId); // Điền vào cột order_detail_id
        pst.setString(2, ct.getOrderId());
        pst.setString(3, ct.getProductId());
        pst.setInt(4, ct.getQuantity());
        pst.setDouble(5, ct.getUnitPrice());

        return pst.executeUpdate();
    }
}

    @Override
    public int insert(OrderDetail t) {
        int kq = 0;
        try (Connection con = DatabaseConnection.getConnection()) {
            kq = insertWithConn(con, t);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return kq;
    }

    // Lấy tất cả chi tiết của 1 hóa đơn cụ thể
    public ArrayList<OrderDetail> selectByOrderId(String orderId) {
        ArrayList<OrderDetail> ds = new ArrayList<>();
        String sql = "SELECT * FROM ORDER_DETAILS WHERE order_id = ?";

        try (Connection con = DatabaseConnection.getConnection(); PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setString(1, orderId);
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    OrderDetail ct = new OrderDetail(
                            rs.getString("order_id"),
                            rs.getString("product_id"),
                            rs.getInt("quantity"),
                            rs.getDouble("unit_price")
                    );
                    ds.add(ct);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ds;
    }

    @Override
    public ArrayList<OrderDetail> selectAll() {
        // Thường ít khi lấy toàn bộ chi tiết của tất cả hóa đơn, 
        // nhưng vẫn để đây để tránh lỗi Interface
        return new ArrayList<>();
    }

    @Override
    public int update(OrderDetail t) {
        return 0;
    }

    @Override
    public int delete(String id) {
        return 0;
    }

    @Override
    public OrderDetail selectById(String id) {
        return null;
    }

    @Override
    public List<OrderDetail> selectByCondition(String condition) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
