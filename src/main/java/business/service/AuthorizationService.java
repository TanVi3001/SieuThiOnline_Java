package business.service;

import java.text.Normalizer;
import java.util.Locale;
import model.account.Account;

public final class AuthorizationService {

    private AuthorizationService() {
    }

    public static boolean isAdmin() {
        return isAdmin(SessionManager.getCurrentUser());
    }

    public static boolean isAdmin(Account account) {
        if (account == null) {
            return false;
        }
        String role = normalize(account.getRole());
        return role.equals("radminall")
                || role.equals("admin")
                || role.equals("quantrivien");
    }

    public static boolean isStaff() {
        return isStaff(SessionManager.getCurrentUser());
    }

    public static boolean isStaff(Account account) {
        if (account == null) {
            return false;
        }
        return !isAdmin(account);
    }

    public static boolean canAccessEmployeeManagement() {
        return isAdmin();
    }

    public static boolean canAccessStatistics() {
        return isAdmin();
    }

    public static String currentRoleForUi() {
        Account account = SessionManager.getCurrentUser();
        return isAdmin(account) ? "ADMIN" : "STAFF";
    }

    private static String normalize(String value) {
        if (value == null) {
            return "";
        }
        String normalized = Normalizer.normalize(value.trim().toLowerCase(Locale.ROOT), Normalizer.Form.NFD);
        return normalized.replaceAll("\\p{M}", "").replaceAll("[^a-z0-9]", "");
    }
}
