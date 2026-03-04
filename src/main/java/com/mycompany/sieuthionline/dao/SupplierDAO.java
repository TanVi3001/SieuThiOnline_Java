package com.mycompany.sieuthionline.dao;

import com.mycompany.sieuthionline.database.JDBCUtil;
import com.mycompany.sieuthionline.model.Supplier;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class SupplierDAO implements DAOInterface<Supplier> {

    // Áp dụng Singleton Pattern
    public static SupplierDAO getInstance() {
        return new SupplierDAO();
    }

    @Override
    public int insert(Supplier t) {
        int ketQua = 0;
        try {
            Connection con = JDBCUtil.getConnection();
            String sql = "INSERT INTO SUPPLIERS (supplier_id, supplier_name, email, address, phone_number, is_deleted) VALUES (?, ?, ?, ?, ?, ?)";
            
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, t.getSupplierId());
            pst.setString(2, t.getSupplierName());
            pst.setString(3, t.getEmail());
            pst.setString(4, t.getAddress());
            pst.setString(5, t.getPhoneNumber());
            pst.setInt(6, t.getIsDeleted());
            
            ketQua = pst.executeUpdate();
            JDBCUtil.closeConnection(con);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ketQua;
    }

    @Override
    public ArrayList<Supplier> selectAll() {
        ArrayList<Supplier> ketQua = new ArrayList<>();
        try {
            Connection con = JDBCUtil.getConnection();
            // Chỉ lấy những nhà cung cấp chưa bị xóa (is_deleted = 0)
            String sql = "SELECT * FROM SUPPLIERS WHERE is_deleted = 0";
            
            PreparedStatement pst = con.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            
            while (rs.next()) {
                String id = rs.getString("supplier_id");
                String name = rs.getString("supplier_name");
                String email = rs.getString("email");
                String address = rs.getString("address");
                String phone = rs.getString("phone_number");
                int isDel = rs.getInt("is_deleted");
                
                Supplier s = new Supplier(id, name, email, address, phone, isDel);
                ketQua.add(s);
            }
            JDBCUtil.closeConnection(con);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ketQua;
    }

    @Override
    public int update(Supplier t) { 
        int ketQua = 0;
        try {
            Connection con = JDBCUtil.getConnection();
            String sql = "UPDATE SUPPLIERS SET supplier_name = ?, email = ?, address = ?, phone_number = ?, is_deleted = ? WHERE supplier_id = ?";
            
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, t.getSupplierName());
            pst.setString(2, t.getEmail());
            pst.setString(3, t.getAddress());
            pst.setString(4, t.getPhoneNumber());
            pst.setInt(5, t.getIsDeleted());
            pst.setString(6, t.getSupplierId());
            
            ketQua = pst.executeUpdate();
            JDBCUtil.closeConnection(con);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ketQua;
    }

    @Override
    public int delete(String id) { 
        int ketQua = 0;
        try {
            Connection con = JDBCUtil.getConnection();
            // Xóa mềm: Cập nhật is_deleted = 1 thay vì xóa vĩnh viễn
            String sql = "UPDATE SUPPLIERS SET is_deleted = 1 WHERE supplier_id = ?";
            
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, id);
            
            ketQua = pst.executeUpdate();
            JDBCUtil.closeConnection(con);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ketQua;
    }

    @Override
    public Supplier selectById(String id) { 
        Supplier ketQua = null;
        try {
            Connection con = JDBCUtil.getConnection();
            String sql = "SELECT * FROM SUPPLIERS WHERE supplier_id = ?";
            
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, id);
            ResultSet rs = pst.executeQuery();
            
            if (rs.next()) {
                String supId = rs.getString("supplier_id");
                String name = rs.getString("supplier_name");
                String email = rs.getString("email");
                String address = rs.getString("address");
                String phone = rs.getString("phone_number");
                int isDel = rs.getInt("is_deleted");
                
                ketQua = new Supplier(supId, name, email, address, phone, isDel);
            }
            JDBCUtil.closeConnection(con);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ketQua;
    }
}