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

// Import DAO (Đã thống nhất dùng bản có s của Tùng và đúng package prod_inventory)
import business.sql.prod_inventory.SuppliersSql;
import business.sql.prod_inventory.StoresSql;
import business.sql.prod_inventory.InventorySql;
import business.sql.sales_order.DeliveryManagementSql;

// Import "Vũ khí" xuất báo cáo
import common.report.ExcelExporter;

public class SieuThiOnline {

    public static void main(String[] args) {
        try {
            // Ép toàn bộ Output của Console về UTF-8 để không lỗi font tiếng Việt
            System.setOut(new java.io.PrintStream(System.out, true, "UTF-8"));
        } catch (java.io.UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        // ==========================================
        // BƯỚC 0: TEST KẾT NỐI ORACLE
        // ==========================================
        System.out.println("=== KIỂM TRA KẾT NỐI ORACLE ===");
        try (Connection con = DatabaseConnection.getConnection()) {
            if (con != null) {
                System.out.println("✅ Chúc mừng! Kết nối Oracle thành công rực rỡ!");
            } else {
                System.out.println("❌ Kết nối thất bại, hãy kiểm tra lại file DatabaseConnection!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // ==========================================
        // BƯỚC 1 - 4: TEST THÊM DỮ LIỆU
        // ==========================================
        System.out.println("\n=== BẮT ĐẦU TEST 4 BẢNG CỦA VĨ ===");
        java.sql.Date ngayHienTai = new java.sql.Date(System.currentTimeMillis());

        // 1. Test bảng SUPPLIERS
        Supplier ncc = new Supplier("SUP_TEST_01", "Công ty Nước Giải Khát", "contact@ngk.com", "KCN Sóng Thần, Dĩ An", "0999888777", 0);
        int kqSup = SuppliersSql.getInstance().insert(ncc);
        if (kqSup > 0) {
            System.out.println("✅ Thành công: Đã thêm SUPPLIERS!");
        } else {
            System.out.println("❌ Thất bại hoặc dữ liệu SUPPLIERS đã tồn tại.");
        }

        // 2. Test bảng STORES
        Store cuaHang = new Store("STORE_TEST_01", "store.langdaihoc@sieuthi.com", "Làng Đại Học Quốc Gia", "0123456789", 0);
        int kqStore = StoresSql.getInstance().insert(cuaHang);
        if (kqStore > 0) {
            System.out.println("✅ Thành công: Đã thêm STORES!");
        } else {
            System.out.println("❌ Thất bại hoặc dữ liệu STORES đã tồn tại.");
        }

        // 3. Test bảng INVENTORY
        Inventory tonKho = new Inventory("PROD_MILK_01", "STORE_TEST_01", 50, "Thùng", ngayHienTai, 0);
        int kqInv = InventorySql.getInstance().insert(tonKho);
        if (kqInv > 0) {
            System.out.println("✅ Thành công: Đã thêm INVENTORY!");
        } else {
            System.out.println("❌ Thất bại hoặc dữ liệu INVENTORY đã tồn tại.");
        }

        // 4. Test bảng DELIVERY_MANAGEMENT
        DeliveryManagement giaoHang = new DeliveryManagement("DEL_TEST_01", "ORD_001", "EMP_01", ngayHienTai, "Đang giao", 0);
        int kqDel = DeliveryManagementSql.getInstance().insert(giaoHang);
        if (kqDel > 0) {
            System.out.println("✅ Thành công: Đã thêm DELIVERY_MANAGEMENT!");
        } else {
            System.out.println("❌ Thất bại hoặc dữ liệu DELIVERY_MANAGEMENT đã tồn tại.");
        }

        // ==========================================
        // BƯỚC 5: TEST XUẤT BÁO CÁO EXCEL
        // ==========================================
        System.out.println("\n=== BƯỚC 5: TEST XUẤT BÁO CÁO EXCEL ===");
        try {
            List<Inventory> dsTonKho = InventorySql.getInstance().selectAll();
            String filePath = "E:\\Inventory_Report_Vi.xlsx";

            ExcelExporter.exportInventory(dsTonKho, filePath);
            System.out.println("🚀 Leader Vĩ vừa xuất xong báo cáo xịn xò tại: " + filePath);
        } catch (Exception e) {
            System.out.println("❌ Lỗi xuất Excel: " + e.getMessage());
        }

        System.out.println("\n=== HOÀN TẤT TOÀN BỘ BÀI TEST ===");
    }
}