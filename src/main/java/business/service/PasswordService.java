package business.service;

import business.sql.rbac.AccountSql;
import org.mindrot.jbcrypt.BCrypt;

import java.util.regex.Pattern;

/**
 * Password helper: check + hash + update DB.
 *
 * Kịch bản: - Nếu hash trong DB bị sửa (không đúng format BCrypt) ->
 * checkPassword trả false (không ném exception). - updatePasswordByAccountId
 * tạo hash mới chuẩn và ghi đè vào DB.
 */
public final class PasswordService {

    private static final int LOG_ROUNDS = 10;

    // Pattern kiểm tra format BCrypt chuẩn (ví dụ: $2a$10$xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx)
    private static final Pattern BCRYPT_PATTERN
            = Pattern.compile("^\\$2[aby]\\$\\d{2}\\$[./A-Za-z0-9]{53}$");

    private PasswordService() {
        /* utility */ }

    /**
     * Kiểm tra xem chuỗi có phải BCrypt hash hợp lệ hay không.
     */
    public static boolean isBCryptHash(String hash) {
        return hash != null && BCRYPT_PATTERN.matcher(hash).matches();
    }

    /**
     * Kiểm tra password: - Nếu hash null/blank hoặc không đúng format -> trả về
     * false (Đăng nhập thất bại). - Nếu BCrypt.checkpw ném
     * IllegalArgumentException (hash bị sửa) -> catch và trả false. - Nếu
     * checkpw trả true -> true.
     */
    public static boolean checkPassword(String plainPassword, String hashFromDb) {
        if (plainPassword == null || plainPassword.isBlank()) {
            return false;
        }
        if (hashFromDb == null || hashFromDb.isBlank()) {
            // Không có hash -> fail
            return false;
        }

        // Kiểm tra format trước (ngăn trường hợp hash bị 'rút gọn' hoặc bị chèn ký tự lạ)
        if (!isBCryptHash(hashFromDb)) {
            // Hash đã bị phá (manual tampering) -> fail, không ném exception ra ngoài
            return false;
        }

        try {
            // Dùng BCrypt.checkpw theo yêu cầu
            return BCrypt.checkpw(plainPassword, hashFromDb);
        } catch (IllegalArgumentException iae) {
            // BCrypt.checkpw sẽ ném IllegalArgumentException nếu hash không đúng format nội bộ
            // Bắt lại và trả false để không làm sập hệ thống
            return false;
        } catch (Exception ex) {
            // Phòng thủ: bất kỳ lỗi runtime nào khác cũng coi là fail
            return false;
        }
    }

    /**
     * Tạo BCrypt hash từ mật khẩu thô.
     */
    public static String hashPassword(String plainPassword) {
        if (plainPassword == null || plainPassword.isBlank()) {
            throw new IllegalArgumentException("Password không được rỗng");
        }
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt(LOG_ROUNDS));
    }

    /**
     * Ghi đè password hash mới theo accountId (dùng cho chức năng Quên mật khẩu
     * / SQL Recovery). Trả về true nếu cập nhật DB thành công.
     *
     * Lưu ý: phương thức này dùng
     * AccountSql.updatePasswordByAccountId(accountId, passwordHash) mà code của
     * bạn đã cung cấp.
     */
    public static boolean updatePasswordByAccountId(String accountId, String newPlainPassword) {
        if (accountId == null || accountId.isBlank()) {
            return false;
        }
        if (newPlainPassword == null || newPlainPassword.isBlank()) {
            return false;
        }

        String newHash = hashPassword(newPlainPassword);
        AccountSql accountSql = AccountSql.getInstance();
        return accountSql.updatePasswordByAccountId(accountId, newHash);
    }

    /**
     * Biến thể: update theo email (nếu muốn). Sử dụng
     * AccountSql.updatePasswordByEmail nếu có sẵn. Trả về true nếu cập nhật
     * thành công.
     */
    public static boolean updatePasswordByEmail(String email, String newPlainPassword) {
        if (email == null || email.isBlank()) {
            return false;
        }
        if (newPlainPassword == null || newPlainPassword.isBlank()) {
            return false;
        }

        // Một số implementation của AccountSql.updatePasswordByEmail đã tự hash bên trong.
        // Nếu của bạn chưa hash trong DB method, có thể thay bằng:
        String newHash = hashPassword(newPlainPassword);
        // Nếu AccountSql có method để cập nhật theo hash, gọi nó; nếu không, dùng updatePasswordByEmail(existing impl)
        AccountSql accountSql = AccountSql.getInstance();
        // Nếu updatePasswordByEmail expects raw password and hashes inside, call:
        // return accountSql.updatePasswordByEmail(email, newPlainPassword);
        // Otherwise, if you have updatePasswordByAccountId, you need to find accountId first.
        // Here we attempt to call existing updatePasswordByEmail (as in your repo):
        return accountSql.updatePasswordByEmail(email, newPlainPassword);
    }
}
