package business.sql.rbac;

import business.sql.SqlInterface;
import common.db.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.account.AuditLog;

public class AuditLogSql implements SqlInterface<AuditLog> {

    public static AuditLogSql getInstance() {
        return new AuditLogSql();
    }

    // =========================================================================
    // HÀM QUAN TRỌNG NHẤT: GHI LOG NẰM TRONG CÙNG 1 TRANSACTION CỦA TÁC VỤ CHÍNH
    // =========================================================================
    public int insertWithConn(Connection con, AuditLog t) throws SQLException {
        // ĐÃ SỬA: Bỏ chữ S trong AUDIT_LOGS -> AUDIT_LOG
        String sql = "INSERT INTO AUDIT_LOG (log_id, account_id, action_type, entity_type, entity_id, "
                + "old_value, new_value, reason, ip_address, device_info, is_deleted) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, 0)";

        try (PreparedStatement pst = con.prepareStatement(sql)) {
            // Tự động sinh Log ID nếu chưa có (Thêm random để tránh trùng lặp khi ghi nhiều log cùng 1 mili-giây)
            String logId = t.getLogId();
            if (logId == null || logId.trim().isEmpty()) {
                logId = "LOG_" + System.currentTimeMillis() + "_" + (int) (Math.random() * 1000);
            }

            pst.setString(1, logId);
            pst.setString(2, t.getAccountId());
            pst.setString(3, t.getActionType());
            pst.setString(4, t.getEntityType());
            pst.setString(5, t.getEntityId());
            pst.setString(6, t.getOldValue());
            pst.setString(7, t.getNewValue());
            pst.setString(8, t.getReason());
            pst.setString(9, t.getIpAddress() != null ? t.getIpAddress() : "localhost");
            pst.setString(10, t.getDeviceInfo());

            return pst.executeUpdate();
        }
    }

    // =========================================================================
    // CÁC HÀM TIÊU CHUẨN CỦA SQL INTERFACE
    // =========================================================================
    @Override
    public int insert(AuditLog t) {
        try (Connection con = DatabaseConnection.getConnection()) {
            return insertWithConn(con, t);
        } catch (SQLException e) {
            System.err.println("Lỗi AuditLogSql.insert: " + e.getMessage());
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int update(AuditLog t) {
        // LƯU Ý NGHIỆP VỤ: Audit Log là lịch sử, KHÔNG NÊN CHO PHÉP UPDATE. 
        // Nhưng vẫn viết hàm để implement Interface
        // ĐÃ SỬA: AUDIT_LOGS -> AUDIT_LOG
        String sql = "UPDATE AUDIT_LOG SET reason = ?, is_deleted = ? WHERE log_id = ?";
        try (Connection con = DatabaseConnection.getConnection(); PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setString(1, t.getReason());
            pst.setInt(2, t.getIsDeleted());
            pst.setString(3, t.getLogId());
            return pst.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Lỗi AuditLogSql.update: " + e.getMessage());
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int delete(String id) {
        // Soft delete
        // ĐÃ SỬA: AUDIT_LOGS -> AUDIT_LOG
        String sql = "UPDATE AUDIT_LOG SET is_deleted = 1 WHERE log_id = ?";
        try (Connection con = DatabaseConnection.getConnection(); PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setString(1, id);
            return pst.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Lỗi AuditLogSql.delete: " + e.getMessage());
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public AuditLog selectById(String id) {
        // ĐÃ SỬA: AUDIT_LOGS -> AUDIT_LOG
        String sql = "SELECT * FROM AUDIT_LOG WHERE log_id = ? AND is_deleted = 0";
        try (Connection con = DatabaseConnection.getConnection(); PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setString(1, id);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    return map(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi AuditLogSql.selectById: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ArrayList<AuditLog> selectAll() {
        ArrayList<AuditLog> list = new ArrayList<>();
        // Lấy danh sách log và sắp xếp mới nhất lên đầu
        // ĐÃ SỬA: AUDIT_LOGS -> AUDIT_LOG
        String sql = "SELECT * FROM AUDIT_LOG WHERE is_deleted = 0 ORDER BY created_at DESC";
        try (Connection con = DatabaseConnection.getConnection(); PreparedStatement pst = con.prepareStatement(sql); ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                list.add(map(rs));
            }
        } catch (SQLException e) {
            System.err.println("Lỗi AuditLogSql.selectAll: " + e.getMessage());
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public List<AuditLog> selectByCondition(String condition) {
        ArrayList<AuditLog> list = new ArrayList<>();
        // ĐÃ SỬA: AUDIT_LOGS -> AUDIT_LOG
        String sql = "SELECT * FROM AUDIT_LOG WHERE is_deleted = 0 " + (condition == null ? "" : condition) + " ORDER BY created_at DESC";
        try (Connection con = DatabaseConnection.getConnection(); PreparedStatement pst = con.prepareStatement(sql); ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                list.add(map(rs));
            }
        } catch (SQLException e) {
            System.err.println("Lỗi AuditLogSql.selectByCondition: " + e.getMessage());
            e.printStackTrace();
        }
        return list;
    }

    // =========================================================================
    // HÀM TÌM KIẾM
    // =========================================================================
    public ArrayList<AuditLog> search(String keyword) {
        ArrayList<AuditLog> list = new ArrayList<>();
        // ĐÃ SỬA: AUDIT_LOGS -> AUDIT_LOG
        String sql = "SELECT * FROM AUDIT_LOG WHERE is_deleted = 0 AND "
                + "(LOWER(action_type) LIKE ? OR LOWER(entity_type) LIKE ? OR LOWER(entity_id) LIKE ? OR LOWER(account_id) LIKE ?) "
                + "ORDER BY created_at DESC";

        try (Connection con = DatabaseConnection.getConnection(); PreparedStatement pst = con.prepareStatement(sql)) {

            String searchPattern = "%" + (keyword == null ? "" : keyword.trim().toLowerCase()) + "%";
            pst.setString(1, searchPattern);
            pst.setString(2, searchPattern);
            pst.setString(3, searchPattern);
            pst.setString(4, searchPattern);

            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    list.add(map(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi AuditLogSql.search: " + e.getMessage());
            e.printStackTrace();
        }
        return list;
    }

    // =========================================================================
    // MAPPING RESULTSET SANG OBJECT
    // =========================================================================
    private AuditLog map(ResultSet rs) throws SQLException {
        AuditLog log = new AuditLog();
        log.setLogId(rs.getString("log_id"));
        log.setAccountId(rs.getString("account_id"));
        log.setActionType(rs.getString("action_type"));
        log.setEntityType(rs.getString("entity_type"));
        log.setEntityId(rs.getString("entity_id"));
        log.setOldValue(rs.getString("old_value"));
        log.setNewValue(rs.getString("new_value"));
        log.setReason(rs.getString("reason"));
        log.setIpAddress(rs.getString("ip_address"));
        log.setDeviceInfo(rs.getString("device_info"));

        try {
            log.setCreatedAt(rs.getTimestamp("created_at"));
        } catch (SQLException ignored) {
        }

        log.setIsDeleted(rs.getInt("is_deleted"));
        return log;
    }

    // =========================================================================
    // HÀM TIỆN ÍCH GHI LOG NHANH (Dùng cho ProductService và các Service khác)
    // =========================================================================
    public void log(String accountId, String actionType, String entityType, String entityId,
            String oldValue, String newValue, String reason,
            String ipAddress, String deviceInfo) {

        AuditLog audit = new AuditLog();
        audit.setAccountId(accountId);
        audit.setActionType(actionType);
        audit.setEntityType(entityType);
        audit.setEntityId(entityId);
        audit.setOldValue(oldValue);
        audit.setNewValue(newValue);
        audit.setReason(reason);
        audit.setIpAddress(ipAddress);
        audit.setDeviceInfo(deviceInfo);

        // Gọi hàm insert chuẩn để lưu xuống DB
        this.insert(audit);
    }
}
