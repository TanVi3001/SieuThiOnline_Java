package business.sql.sales_order;

import business.sql.SqlInterface;
import common.db.DatabaseConnection;
import model.DeliveryManagement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class DeliveryManagementSql implements SqlInterface<DeliveryManagement> {

    public static DeliveryManagement getInstance() {
        return new DeliveryManagement();
    }

    @Override
    public int insert(DeliveryManagement t) {
        int ketQua = 0;
        try {
            Connection con = DatabaseConnection.getConnection();
            String sql = "INSERT INTO DELIVERY_MANAGEMENT (delivery_id, order_id, employee_id, execution_date, status, is_deleted) VALUES (?, ?, ?, ?, ?, ?)";
            
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, t.getDeliveryId());
            pst.setString(2, t.getOrderId());
            pst.setString(3, t.getEmployeeId());
            pst.setDate(4, t.getExecutionDate()); 
            pst.setString(5, t.getStatus());
            pst.setInt(6, t.getIsDeleted());
            
            ketQua = pst.executeUpdate();
            DatabaseConnection.closeConnection(con);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ketQua;
    }

    @Override public ArrayList<DeliveryManagement> selectAll() { return new ArrayList<>(); }
    @Override public int update(DeliveryManagement t) { return 0; }
    @Override public int delete(String id) { return 0; }
    @Override public DeliveryManagement selectById(String id) { return null; }
}