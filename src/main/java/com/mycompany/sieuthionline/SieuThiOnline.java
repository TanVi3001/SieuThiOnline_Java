package com.mycompany.sieuthionline;

import com.mycompany.sieuthionline.database.JDBCUtil;
import java.sql.Connection;

public class SieuThiOnline {

    public static void main(String[] args) {
        // Gọi hàm kết nối
        Connection con = JDBCUtil.getConnection();
        
        // Kiểm tra kết quả
        if (con != null) {
            System.out.println("Chúc mừng! Kết nối Oracle thành công!");
            JDBCUtil.closeConnection(con);
        } else {
            System.out.println("Kết nối thất bại. Hãy kiểm tra lại username, password hoặc xem Oracle đã bật chưa.");
        }
    }
}