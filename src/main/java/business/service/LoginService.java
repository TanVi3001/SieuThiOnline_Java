package business.service;

import business.sql.rbac.AccountSql;
import common.security.PasswordUtil;
import model.account.Account;

public class LoginService {

    private static Account currentUser;

    /**
     * Xác thực đăng nhập:
     * - Nếu DB lưu BCrypt -> verify BCrypt
     * - Nếu DB còn plain text -> so khớp plain, nếu đúng thì auto-upgrade sang BCrypt
     *
     * @param username tên đăng nhập
     * @param password mật khẩu người dùng nhập
     * @return true nếu đăng nhập thành công, ngược lại false
     */
    public static boolean authenticate(String username, String password) {
        System.out.println("DEBUG: Dang nhap voi User = [" + username + "]");

        AccountSql accountSql = AccountSql.getInstance();
        Account acc = accountSql.selectByUsername(username);

        if (acc == null) {
            System.out.println("❌ Đăng nhập thất bại: Tài khoản không tồn tại.");
            return false;
        }

        String stored = acc.getPassword();
        if (stored == null || stored.isEmpty()) {
            System.out.println("❌ Đăng nhập thất bại: Tài khoản chưa có mật khẩu hợp lệ.");
            return false;
        }

        boolean ok;
        if (PasswordUtil.isBCryptHash(stored)) {
            // DB đã là hash
            ok = PasswordUtil.verify(password, stored);
        } else {
            // DB còn plain text (legacy)
            ok = stored.equals(password);

            // Auto-upgrade sang hash nếu login đúng
            if (ok) {
                String newHash = PasswordUtil.hash(password);
                boolean upgraded = accountSql.updatePasswordByAccountId(acc.getAccountId(), newHash);
                if (upgraded) {
                    acc.setPassword(newHash); // cập nhật object hiện tại
                    System.out.println("INFO: Đã auto-upgrade mật khẩu sang BCrypt cho account: " + acc.getAccountId());
                } else {
                    System.out.println("WARN: Không auto-upgrade được mật khẩu (DB update fail).");
                }
            }
        }

        if (ok) {
            currentUser = acc;
            System.out.println("✅ Đăng nhập thành công! Chào mừng " + acc.getUsername());
            return true;
        } else {
            System.out.println("❌ Đăng nhập thất bại: Sai mật khẩu.");
            return false;
        }
    }

    /**
     * Lấy user đang đăng nhập.
     *
     * @return account hiện tại hoặc null
     */
    public static Account getCurrentUser() {
        return currentUser;
    }

    /**
     * Đăng xuất.
     */
    public static void logout() {
        currentUser = null;
        System.out.println("LOG: Người dùng đã đăng xuất.");
    }

    /**
     * Tạm thời trả false vì schema hiện tại không có role trực tiếp trong ACCOUNTS.
     *
     * @return false
     */
    public static boolean isAdmin() {
        return false;
    }
}