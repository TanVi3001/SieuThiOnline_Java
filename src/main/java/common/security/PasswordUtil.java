package common.security;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtil {

    private PasswordUtil() {}

    // Băm mật khẩu thô
    public static String hash(String rawPassword) {
        return BCrypt.hashpw(rawPassword, BCrypt.gensalt(12));
    }

    // So khớp mật khẩu thô với hash trong DB
    public static boolean verify(String rawPassword, String hashedPassword) {
        if (rawPassword == null || hashedPassword == null) return false;
        return BCrypt.checkpw(rawPassword, hashedPassword);
    }

    // Kiểm tra chuỗi có phải hash BCrypt không
    public static boolean isBCryptHash(String s) {
        if (s == null) return false;
        return s.startsWith("$2a$") || s.startsWith("$2b$") || s.startsWith("$2y$");
    }
}