package business.service;

import business.sql.rbac.AccountSql;
import business.sql.rbac.TokenSql;
import common.security.PasswordUtil;
import model.account.Account;
import model.account.Token;

import java.sql.Timestamp;
import java.util.UUID;

public class LoginService {

    /**
     * Xác thực đăng nhập: - Nếu DB lưu BCrypt -> verify BCrypt - Nếu DB còn
     * plain text -> so khớp plain, nếu đúng thì auto-upgrade sang BCrypt -
     * Thành công: tạo token + lưu DB TOKENS + lưu session + trả về Account
     * @param username
     * @param password
     * @return 
     */
    public static Account authenticate(String username, String password) {
        System.out.println("DEBUG: Dang nhap voi User = [" + username + "]");

        AccountSql accountSql = AccountSql.getInstance();
        Account acc = accountSql.selectByUsername(username);

        if (acc == null) {
            System.out.println("❌ Đăng nhập thất bại: Tài khoản không tồn tại.");
            return null;
        }

        String stored = acc.getPassword();
        if (stored == null || stored.isEmpty()) {
            System.out.println("❌ Đăng nhập thất bại: Tài khoản chưa có mật khẩu hợp lệ.");
            return null;
        }

        boolean ok;
        if (PasswordUtil.isBCryptHash(stored)) {
            ok = PasswordUtil.verify(password, stored);
        } else {
            ok = stored.equals(password);

            // Auto-upgrade sang BCrypt nếu login đúng
            if (ok) {
                String newHash = PasswordUtil.hash(password);
                boolean upgraded = accountSql.updatePasswordByAccountId(acc.getAccountId(), newHash);
                if (upgraded) {
                    acc.setPassword(newHash);
                    System.out.println("INFO: Da auto-upgrade mat khau sang BCrypt cho account: " + acc.getAccountId());
                } else {
                    System.out.println("WARN: Khong auto-upgrade duoc mat khau (DB update fail).");
                }
            }
        }

        if (!ok) {
            System.out.println("❌ Đăng nhập thất bại: Sai mật khẩu.");
            return null;
        }

        // 1) Tạo token
        String tokenValue = UUID.randomUUID().toString();

        // 2) Gán vào Account object (để UI dùng)
        acc.setToken(tokenValue);

        // 3) Lưu token xuống bảng TOKENS
        Token token = new Token();
        token.setTokenId(UUID.randomUUID().toString()); 
        token.setAccountId(acc.getAccountId());
        token.setTokenValue(tokenValue);

        // Hạn dùng 45 phút
        long now = System.currentTimeMillis();
        token.setExpiryDate(new java.sql.Timestamp(now + 45L * 60 * 1000));

        int inserted = TokenSql.getInstance().insert(token);
        if (inserted <= 0) {
            System.out.println("WARN: Login OK nhung luu token DB that bai.");
            // vẫn cho login vào nếu bạn muốn "mềm"
            // nếu muốn chặt, return null ở đây
        } else {
            System.out.println("INFO: Da luu token vao DB thanh cong.");
        }

        // 4) Lưu session RAM
        SessionManager.startSession(acc, tokenValue);

        System.out.println("✅ Đăng nhập thành công! Chào mừng " + acc.getUsername());
        return acc;
    }

    public static Account getCurrentUser() {
        return SessionManager.getCurrentUser();
    }

    public static String getToken() {
        return SessionManager.getToken();
    }

    /**
     * Đăng xuất: - Revoke token trong DB - Clear session RAM
     */
    public static void logout() {
        String currentToken = SessionManager.getToken();

        if (currentToken != null && !currentToken.isBlank()) {
            int revoked = TokenSql.getInstance().revokeToken(currentToken);
            System.out.println("INFO: revoke token rows = " + revoked);
        }

        SessionManager.clear();
        System.out.println("LOG: Người dùng đã đăng xuất.");
    }

    public static boolean isAdmin() {
        Account u = getCurrentUser();
        return u != null && u.getRole() != null && u.getRole().equalsIgnoreCase("admin");
    }
}
