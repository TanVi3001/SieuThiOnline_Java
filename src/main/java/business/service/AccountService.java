package business.service;

import business.sql.rbac.AuditLogSql;

public class AccountService {

    /**
     * Ghi audit log cho hành động đổi role user. Gọi hàm này sau khi update
     * role thành công trong AccountSql.
     * @param targetAccountId
     * @param oldRole
     * @param newRole
     * @param reason
     */
    public static void logChangeRole(String targetAccountId, String oldRole, String newRole, String reason) {
        String actorId = SessionManager.getCurrentUser() != null
                ? SessionManager.getCurrentUser().getAccountId()
                : null;

        AuditLogSql.getInstance().log(
                actorId,
                "CHANGE_ROLE",
                "ACCOUNT",
                targetAccountId,
                "role=" + (oldRole != null ? oldRole : "UNKNOWN"),
                "role=" + (newRole != null ? newRole : "UNKNOWN"),
                reason != null ? reason : "Admin cap nhat quyen",
                localIp(),
                deviceInfo()
        );
    }

    private static String localIp() {
        try {
            return java.net.InetAddress.getLocalHost().getHostAddress();
        } catch (Exception e) {
            return "unknown";
        }
    }

    private static String deviceInfo() {
        return System.getProperty("os.name") + " | Java " + System.getProperty("java.version");
    }
}
