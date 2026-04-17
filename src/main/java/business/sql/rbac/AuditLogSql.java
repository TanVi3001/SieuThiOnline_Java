package business.sql.rbac;

import common.db.DatabaseConnection;
import model.account.AuditLog;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;

public class AuditLogSql {

    private static AuditLogSql instance;

    private AuditLogSql() {
    }

    public static AuditLogSql getInstance() {
        if (instance == null) {
            instance = new AuditLogSql();
        }
        return instance;
    }

    public int insert(AuditLog a) {
        try (Connection con = DatabaseConnection.getConnection()) {
            return insertWithConn(con, a); // tái sử dụng cùng 1 logic
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * Insert audit log bằng connection truyền vào (dùng chung transaction).
     * @param con
     * @param a
     * @return 
     * @throws java.sql.SQLException
     */
    public int insertWithConn(Connection con, AuditLog a) throws SQLException {
        String sql = "INSERT INTO AUDIT_LOG "
                + "(LOG_ID, ACCOUNT_ID, ACTION_TYPE, ENTITY_TYPE, ENTITY_ID, OLD_VALUE, NEW_VALUE, REASON, IP_ADDRESS, DEVICE_INFO, CREATED_AT, IS_DELETED) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, SYSTIMESTAMP, 0)";

        try (PreparedStatement pst = con.prepareStatement(sql)) {
            String logId = (a.getLogId() == null || a.getLogId().isBlank())
                    ? UUID.randomUUID().toString()
                    : a.getLogId();

            pst.setString(1, logId);
            pst.setString(2, a.getAccountId());
            pst.setString(3, a.getActionType());
            pst.setString(4, a.getEntityType());
            pst.setString(5, a.getEntityId());
            pst.setString(6, a.getOldValue());
            pst.setString(7, a.getNewValue());
            pst.setString(8, a.getReason());
            pst.setString(9, a.getIpAddress());
            pst.setString(10, a.getDeviceInfo());

            System.out.println("AUDIT DEBUG: action=" + a.getActionType() + ", entity=" + a.getEntityId());
            int rows = pst.executeUpdate();
            System.out.println("AUDIT INSERT rows=" + rows);
            return rows;
        }
    }

    public void log(String accountId, String actionType, String entityType, String entityId,
            String oldValue, String newValue, String reason, String ip, String device) {
        AuditLog a = new AuditLog();
        a.setAccountId(accountId);
        a.setActionType(actionType);
        a.setEntityType(entityType);
        a.setEntityId(entityId);
        a.setOldValue(oldValue);
        a.setNewValue(newValue);
        a.setReason(reason);
        a.setIpAddress(ip);
        a.setDeviceInfo(device);
        insert(a);
    }
}
