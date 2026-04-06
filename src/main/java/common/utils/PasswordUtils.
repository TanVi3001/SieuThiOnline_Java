package com.mycompany.sieuthionline.common.utils;

import org.mindrot.jbcrypt.BCrypt;

public final class PasswordUtils {

    // Có thể tăng lên 12 nếu muốn bảo mật cao hơn (đổi lại hash chậm hơn)
    private static final int LOG_ROUNDS = 10;

    private PasswordUtils() {
        // Utility class -> không cho khởi tạo
    }

    /**
     * Băm mật khẩu dạng plain text bằng BCrypt.
     * @param plainText mật khẩu gốc người dùng nhập
     * @return chuỗi hash BCrypt để lưu DB
     */
    public static String hashPassword(String plainText) {
        if (plainText == null || plainText.isBlank()) {
            throw new IllegalArgumentException("Password không được null hoặc rỗng");
        }
        return BCrypt.hashpw(plainText, BCrypt.gensalt(LOG_ROUNDS));
    }

    /**
     * So khớp mật khẩu plain text với hash trong DB.
     * @param plainText mật khẩu người dùng nhập
     * @param hashed mật khẩu đã băm lưu trong DB
     * @return true nếu khớp, false nếu không khớp
     */
    public static boolean checkPassword(String plainText, String hashed) {
        if (plainText == null || plainText.isBlank() || hashed == null || hashed.isBlank()) {
            return false;
        }

        try {
            return BCrypt.checkpw(plainText, hashed);
        } catch (IllegalArgumentException ex) {
            // hashed không đúng format BCrypt
            return false;
        }
    }
}
