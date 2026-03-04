package com.mycompany.sieuthionline.dao;

import com.mycompany.sieuthionline.database.JDBCUtil;
import com.mycompany.sieuthionline.model.Delivery;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class DeliveryDAO implements DAOInterface<Delivery> {

    public static DeliveryDAO getInstance() {
        return new DeliveryDAO();
    }

    @Override
    public int insert(Delivery t) {
        int ketQua = 0;
        try {
            Connection con = JDBCUtil.getConnection();
            String sql = "INSERT INTO DELIVERY_MANAGEMENT (delivery_id, order_id, employee_id, execution_date, status, is_deleted) VALUES (?, ?, ?, ?, ?, ?)";
            
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, t.getDeliveryId());
            pst.setString(2, t.getOrderId());
            pst.setString(3, t.getEmployeeId());
            pst.setDate(4, t.getExecutionDate()); // Ép kiểu ngày tháng java.sql.Date
            pst.setString(5, t.getStatus());
            pst.setInt(6, t.getIsDeleted());
            
            ketQua = pst.executeUpdate();
            JDBCUtil.closeConnection(con);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ketQua;
    }

    @Override
    public ArrayList<Delivery> selectAll() {
        ArrayList<Delivery> ketQua = new ArrayList<>();
        try {
            Connection con = JDBCUtil.getConnection();
            // Lấy các đơn giao hàng chưa bị xóa
            String sql = "SELECT * FROM DELIVERY_MANAGEMENT WHERE is_deleted = 0";
            
            PreparedStatement pst = con.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            
            while (rs.next()) {
                Delivery d = new Delivery(
                    rs.getString("delivery_id"),
                    rs.getString("order_id"),
                    rs.getString("employee_id"),
                    rs.getDate("execution_date"),
                    rs.getString("status"),
                    rs.getInt("is_deleted")
                );
                ketQua.add(d);
            }
            JDBCUtil.closeConnection(con);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ketQua;
    }

    @Override
    public int update(Delivery t) { 
        int ketQua = 0;
        try {
            Connection con = JDBCUtil.getConnection();
            String sql = "UPDATE DELIVERY_MANAGEMENT SET order_id = ?, employee_id = ?, execution_date = ?, status = ?, is_deleted = ? WHERE delivery_id = ?";
            
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, t.getOrderId());
            pst.setString(2, t.getEmployeeId());
            pst.setDate(3, t.getExecutionDate());
            pst.setString(4, t.getStatus());
            pst.setInt(5, t.getIsDeleted());
            pst.setString(6, t.getDeliveryId()); // ID nằm ở cuối câu lệnh Update
            
            ketQua = pst.executeUpdate();
            JDBCUtil.closeConnection(con);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ketQua;
    }

    @Override
    public int delete(String id) { 
        int ketQua = 0;
        try {
            Connection con = JDBCUtil.getConnection();
            // Xóa mềm: Chuyển is_deleted = 1
            String sql = "UPDATE DELIVERY_MANAGEMENT SET is_deleted = 1 WHERE delivery_id = ?";
            
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, id);
            
            ketQua = pst.executeUpdate();
            JDBCUtil.closeConnection(con);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ketQua;
    }

    @Override
    public Delivery selectById(String id) { 
        Delivery ketQua = null;
        try {
            Connection con = JDBCUtil.getConnection();
            String sql = "SELECT * FROM DELIVERY_MANAGEMENT WHERE delivery_id = ?";
            
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, id);
            ResultSet rs = pst.executeQuery();
            
            if (rs.next()) {
                ketQua = new Delivery(
                    rs.getString("delivery_id"),
                    rs.getString("order_id"),
                    rs.getString("employee_id"),
                    rs.getDate("execution_date"),
                    rs.getString("status"),
                    rs.getInt("is_deleted")
                );
            }
            JDBCUtil.closeConnection(con);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ketQua;
    }
}