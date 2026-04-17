package business.sql.rbac;

import business.sql.SqlInterface;
import common.db.DatabaseConnection;
import model.account.LoginHistory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class LoginHistorySql implements SqlInterface<LoginHistory> {

    public static LoginHistorySql getInstance() {
        return new LoginHistorySql();
    }

    @Override
    public int insert(LoginHistory h) {
        String sql = "INSERT INTO LOGIN_HISTORY "
                + "(LOG_ID, ACCOUNT_ID, ACTION_TYPE, IP_ADDRESS, DEVICE_INFO, LOGIN_TIME, STATUS, FAILURE_REASON, IS_DELETED) "
                + "VALUES (?, ?, ?, ?, ?, SYSTIMESTAMP, ?, ?, 0)";
        try (Connection con = DatabaseConnection.getConnection(); PreparedStatement pst = con.prepareStatement(sql)) {

            String logId = (h.getLogId() == null || h.getLogId().isBlank())
                    ? UUID.randomUUID().toString()
                    : h.getLogId();

            pst.setString(1, logId);
            pst.setString(2, h.getAccountId()); // có thể null nếu account chưa xác định
            pst.setString(3, h.getActionType());
            pst.setString(4, h.getIpAddress());
            pst.setString(5, h.getDeviceInfo());
            pst.setString(6, h.getStatus());
            pst.setString(7, h.getFailureReason());

            return pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int update(LoginHistory h) {
        String sql = "UPDATE LOGIN_HISTORY SET "
                + "ACCOUNT_ID = ?, ACTION_TYPE = ?, IP_ADDRESS = ?, DEVICE_INFO = ?, "
                + "STATUS = ?, FAILURE_REASON = ?, IS_DELETED = ? "
                + "WHERE LOG_ID = ?";
        try (Connection con = DatabaseConnection.getConnection(); PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setString(1, h.getAccountId());
            pst.setString(2, h.getActionType());
            pst.setString(3, h.getIpAddress());
            pst.setString(4, h.getDeviceInfo());
            pst.setString(5, h.getStatus());
            pst.setString(6, h.getFailureReason());
            pst.setInt(7, h.getIsDeleted());
            pst.setString(8, h.getLogId());

            return pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int delete(String id) {
        // soft delete
        String sql = "UPDATE LOGIN_HISTORY SET IS_DELETED = 1 WHERE LOG_ID = ?";
        try (Connection con = DatabaseConnection.getConnection(); PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setString(1, id);
            return pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public LoginHistory selectById(String id) {
        String sql = "SELECT * FROM LOGIN_HISTORY WHERE LOG_ID = ? AND IS_DELETED = 0";
        try (Connection con = DatabaseConnection.getConnection(); PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setString(1, id);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    return map(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ArrayList<LoginHistory> selectAll() {
        ArrayList<LoginHistory> list = new ArrayList<>();
        String sql = "SELECT * FROM LOGIN_HISTORY WHERE IS_DELETED = 0 ORDER BY LOGIN_TIME DESC";
        try (Connection con = DatabaseConnection.getConnection(); PreparedStatement pst = con.prepareStatement(sql); ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                list.add(map(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public List<LoginHistory> selectByCondition(String condition) {
        ArrayList<LoginHistory> list = new ArrayList<>();
        String sql = "SELECT * FROM LOGIN_HISTORY WHERE " + condition;
        try (Connection con = DatabaseConnection.getConnection(); PreparedStatement pst = con.prepareStatement(sql); ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                list.add(map(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // ===== helper business methods =====
    public ArrayList<LoginHistory> selectByAccountId(String accountId) {
        ArrayList<LoginHistory> list = new ArrayList<>();
        String sql = "SELECT * FROM LOGIN_HISTORY WHERE ACCOUNT_ID = ? AND IS_DELETED = 0 ORDER BY LOGIN_TIME DESC";
        try (Connection con = DatabaseConnection.getConnection(); PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setString(1, accountId);
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    list.add(map(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public int softDeleteByAccountId(String accountId) {
        String sql = "UPDATE LOGIN_HISTORY SET IS_DELETED = 1 WHERE ACCOUNT_ID = ?";
        try (Connection con = DatabaseConnection.getConnection(); PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setString(1, accountId);
            return pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    // helper log nhanh
    public void log(String accountId, String actionType, String status, String reason, String ip, String device) {
        LoginHistory h = new LoginHistory();
        h.setAccountId(accountId);
        h.setActionType(actionType);
        h.setStatus(status);
        h.setFailureReason(reason);
        h.setIpAddress(ip);
        h.setDeviceInfo(device);
        insert(h);
    }

    private LoginHistory map(ResultSet rs) throws SQLException {
        LoginHistory h = new LoginHistory();
        h.setLogId(rs.getString("LOG_ID"));
        h.setAccountId(rs.getString("ACCOUNT_ID"));
        h.setActionType(rs.getString("ACTION_TYPE"));
        h.setIpAddress(rs.getString("IP_ADDRESS"));
        h.setDeviceInfo(rs.getString("DEVICE_INFO"));
        h.setLoginTime(rs.getTimestamp("LOGIN_TIME"));
        h.setStatus(rs.getString("STATUS"));
        h.setFailureReason(rs.getString("FAILURE_REASON"));
        h.setIsDeleted(rs.getInt("IS_DELETED"));
        return h;
    }
}
