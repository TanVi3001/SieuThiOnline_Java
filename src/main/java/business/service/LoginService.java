package business.service;

import business.sql.rbac.AccountSql;
import model.account.Account;

public class LoginService {

    // Biến static để lưu thông tin người dùng đang đăng nhập (Session giả lập)
    private static Account currentUser;

    /**
     * Hàm xử lý đăng nhập
     *
     * @param username tên đăng nhập từ LoginView
     * @param password mật khẩu từ LoginView
     * @return true nếu khớp, false nếu sai tài khoản/mật khẩu
     */
    public static boolean authenticate(String username, String password) {
        System.out.println("DEBUG: Dang nhap voi User = [" + username + "] | Pass = [" + password + "]");
        // 1. Gọi xuống SQL để lấy thông tin tài khoản theo username
        // Giả sử AccountSql đã có hàm selectByUsername hoặc dùng selectById nếu username là ID
        Account acc = AccountSql.getInstance().selectByUsername(username);

        // 2. Kiểm tra tài khoản có tồn tại không
        if (acc == null) {
            System.out.println("❌ Đăng nhập thất bại: Tài khoản không tồn tại.");
            return false;
        }

        // 3. So khớp mật khẩu (Ở mức độ đồ án, AE mình so khớp chuỗi trực tiếp)
        if (acc.getPassword().equals(password)) {
            currentUser = acc; // Lưu lại phiên đăng nhập
            System.out.println("✅ Đăng nhập thành công! Chào mừng " + acc.getUsername() + " [" + acc.getRole() + "]");
            return true;
        } else {
            System.out.println("❌ Đăng nhập thất bại: Sai mật khẩu.");
            return false;
        }
    }

    /**
     * Hàm lấy thông tin người dùng hiện tại
     *
     * @return
     */
    public static Account getCurrentUser() {
        return currentUser;
    }

    /**
     * Hàm đăng xuất
     */
    public static void logout() {
        currentUser = null;
        System.out.println("LOG: Người dùng đã đăng xuất.");
    }

    /**
     * Hàm kiểm tra quyền Admin để hỗ trợ Quỳnh ẩn/hiện nút
     *
     * @return
     */
    public static boolean isAdmin() {
        return currentUser != null && "ADMIN".equalsIgnoreCase(currentUser.getRole());
    }
}
