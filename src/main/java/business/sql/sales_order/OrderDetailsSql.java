package business.sql.sales_order;

import model.OrderDetail;
import business.sql.SqlInterface;
import java.util.ArrayList;

public class OrderDetailsSql implements SqlInterface<OrderDetail> {
    public static OrderDetailsSql getInstance() { return new OrderDetailsSql(); }

    @Override public int insert(OrderDetail t) { return 0; }
    @Override public int update(OrderDetail t) { return 0; }
    @Override public int delete(String id) { return 0; }
    @Override public ArrayList<OrderDetail> selectAll() { return new ArrayList<>(); }
    
    // Hàm đặc thù: Lấy tất cả chi tiết của 1 hóa đơn
    public ArrayList<OrderDetail> selectByOrderId(String orderId) { return new ArrayList<>(); }

    @Override
    public OrderDetail selectById(String id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}