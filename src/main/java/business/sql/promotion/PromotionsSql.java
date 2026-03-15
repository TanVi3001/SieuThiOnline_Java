package business.sql.promotion;

import model.Promotion; 
import business.sql.SqlInterface;
import common.db.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PromotionsSql implements SqlInterface<Promotion> {

    public static PromotionsSql getInstance() {
        return new PromotionsSql();
    }

    @Override
    public int insert(Promotion t) {
        int ketQua = 0;
        String sql = "INSERT INTO PROMOTIONS (promotion_id, promotion_name, campaign_id, application_condition, status, order_detail_id, discount_amount, is_deleted) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setString(1, t.getPromotionId());
            pst.setString(2, t.getPromotionName());
            pst.setString(3, t.getCampaignId());
            pst.setString(4, t.getApplicationCondition());
            pst.setString(5, t.getStatus());
            pst.setString(6, t.getOrderDetailId());
            pst.setDouble(7, t.getDiscountAmount());
            pst.setInt(8, t.getIsDeleted());
            ketQua = pst.executeUpdate();
            DatabaseConnection.closeConnection(con);
        } catch (SQLException e) { e.printStackTrace(); }
        return ketQua;
    }

    @Override public int update(Promotion t) { return 0; }
    @Override public int delete(String id) { return 0; }
    @Override public ArrayList<Promotion> selectAll() { return new ArrayList<>(); }
    @Override public Promotion selectById(String id) { return null; }
    
    // Hàm bổ trợ: Lấy danh sách khuyến mãi đang còn hiệu lực
    public ArrayList<Promotion> selectActivePromotions() { return new ArrayList<>(); }

    @Override
    public List<Promotion> selectByCondition(String condition) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}