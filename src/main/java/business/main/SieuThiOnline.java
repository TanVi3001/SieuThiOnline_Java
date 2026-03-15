package business.main;

// Import thư viện hệ thống
import common.db.DatabaseConnection;
import java.sql.Connection;
import java.util.List;

// Import Model
import model.Supplier;
import model.Store;
import model.Inventory;
import model.DeliveryManagement;
import model.Order;
import model.OrderDetail;

import business.sql.prod_inventory.SuppliersSql;
import business.sql.prod_inventory.StoresSql;
import business.sql.prod_inventory.InventorySql;
import business.sql.sales_order.DeliveryManagementSql;
import business.service.PaymentService; 

// Import xuất báo cáo
import common.report.ExcelExporter;
import java.util.ArrayList;

/**
 * Dự án: Hệ thống Quản lý Siêu thị Online
 * Mô tả: Module kiểm thử tích hợp các thành phần hệ thống
 * Người thực hiện: Lê Tấn Vĩ (Nhóm trưởng)
 */
public class SieuThiOnline {

    public static void main(String[] args) {
        // Thiết lập luồng xuất dữ liệu chuẩn UTF-8 để hiển thị Tiếng Việt
        try {
            System.setOut(new java.io.PrintStream(System.out, true, "UTF-8"));
        } catch (java.io.UnsupportedEncodingException e) {
            System.err.println("[LỖI] Hệ thống không hỗ trợ định dạng UTF-8.");
        }

        System.out.println("-------------------------------------------------------");
        System.out.println("BẮT ĐẦU KIỂM THỬ TÍCH HỢP HỆ THỐNG");
        System.out.println("-------------------------------------------------------");

        // =====================================================
        // GIAI ĐOẠN 0: KIỂM TRA KẾT NỐI CƠ SỞ DỮ LIỆU
        // =====================================================
        System.out.println("[THÔNG BÁO] Đang thực thi Giai đoạn 0: Kiểm tra kết nối Oracle...");
        try (Connection con = DatabaseConnection.getConnection()) {
            if (con != null) {
                System.out.println("[HOÀN TẤT] Kết nối cơ sở dữ liệu được thiết lập thành công.");
            } else {
                System.out.println("[THẤT BẠI] Không thể thiết lập kết nối cơ sở dữ liệu.");
            }
        } catch (Exception e) {
            System.out.println("[CẢNH BÁO] Ngoại lệ kết nối: " + e.getMessage());
        }

        // =====================================================
        // GIAI ĐOẠN 5: KIỂM TRA MODULE XUẤT BÁO CÁO EXCEL
        // =====================================================
        System.out.println("\n[THÔNG BÁO] Đang thực thi Giai đoạn 5: Kiểm tra xuất báo cáo kho...");
        try {
            List<Inventory> dsTonKho = InventorySql.getInstance().selectAll();
            String filePath = "E:\\Inventory_Report_Vi.xlsx";

            ExcelExporter.exportInventory(dsTonKho, filePath);
            System.out.println("[HOÀN TẤT] Báo cáo hàng tồn kho đã được xuất tại: " + filePath);
        } catch (Exception e) {
            System.out.println("[CẢNH BÁO] Lỗi xuất dữ liệu Excel: " + e.getMessage());
        }

        // =====================================================
        // GIAI ĐOẠN 6: KIỂM TRA GIAO DỊCH THANH TOÁN (LOGIC CỐT LÕI)
        // =====================================================
        System.out.println("\n[THÔNG BÁO] Đang thực thi Giai đoạn 6: Kiểm tra giao dịch thanh toán...");

        // Định danh duy nhất cho mã hóa đơn kiểm thử
        String maHD = "HD_ST_1002"; 

        // Khởi tạo dữ liệu Hóa đơn
        Order hd = new Order(
                maHD,
                "KH001", 
                "EMP_01", 
                new java.sql.Date(System.currentTimeMillis()),
                150000.0,
                "ĐÃ THANH TOÁN"
        );

        // Khởi tạo danh sách Chi tiết hóa đơn (Giỏ hàng)
        List<OrderDetail> gioHang = new ArrayList<>();
        gioHang.add(new OrderDetail(maHD, "PROD_MILK_01", 2, 20000.0));
        gioHang.add(new OrderDetail(maHD, "PROD_MILK_02", 5, 8000.0));

        // Thực thi giao dịch thông qua PaymentService (Áp dụng Transaction)
        try {
            boolean isTransactionSuccess = PaymentService.thanhToan(hd, gioHang);

            if (isTransactionSuccess) {
                System.out.println("[HOÀN TẤT] Giao dịch thành công: Đã lưu hóa đơn và cập nhật kho hàng.");
            } else {
                System.out.println("[THẤT BẠI] Giao dịch không thành công: Cơ chế Rollback đã được kích hoạt.");
            }
        } catch (Exception e) {
            System.out.println("[NGHIÊM TRỌNG] Lỗi hệ thống trong quá trình giao dịch: " + e.getMessage());
        }

        System.out.println("\n-------------------------------------------------------");
        System.out.println("KẾT THÚC QUY TRÌNH KIỂM THỬ TÍCH HỢP");
        System.out.println("-------------------------------------------------------");
    }
}